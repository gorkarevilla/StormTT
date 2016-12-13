/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import twitter4j.HashtagEntity;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
/**
 * 
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class TwitterApp {

	public static String mode;
	public static String apiKey;
	public static String apiSecret;
	public static String tokenValue;
	public static String tokenSecret;
	public static String kafkaBrokerURL;
	public static String inputFile;
	public static KafkaBrokerProducer kafkaProducer;

	private static final Boolean CREATEJSON = false;
	private static final String OUTPUTJSON = "twitterOutput.json";

	public static void main(String[] args) {

		if (args.length == 7) {
			mode = args[0];
			apiKey = args[1];
			apiSecret = args[2];
			tokenValue = args[3];
			tokenSecret = args[4];
			kafkaBrokerURL = args[5];
			inputFile = args[6];
		} else {
			printUsage();
		}

		kafkaProducer = new KafkaBrokerProducer();

		if (mode.equals("1")) { // FILE PART
			FileInputStream fstream = null;
			BufferedReader br = null;
			try {
				// Open the file
				fstream = new FileInputStream(inputFile);
				br = new BufferedReader(new InputStreamReader(fstream));
				String strLine;
				if(Top3App.INFO)
					System.out.println("Reading from File "+inputFile+"...");
				while ((strLine = br.readLine()) != null) {
					Status tweet = TwitterObjectFactory.createStatus(strLine);
					readStatus(tweet);
					}

				

			} catch (FileNotFoundException ex) {
				System.err.println("Your file couldn't be found");
				printUsage();
			} catch (IOException ex) {
				System.err.println("Error opening the file");
			
			} catch (TwitterException e) {
				System.err.println("Error reading from file");
			} finally {
				try {
					fstream.close();
					br.close();
				} catch (IOException ex) {
					System.err.println("Error closing the file");
				}
			}
		} else if (mode.equals("2")) { // TWITTER PART
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setJSONStoreEnabled(true);
			cb.setDebugEnabled(true).setOAuthConsumerKey(apiKey).setOAuthConsumerSecret(apiSecret)
					.setOAuthAccessToken(tokenValue).setOAuthAccessTokenSecret(tokenSecret);

			if(Top3App.INFO)
				System.out.println("Reading Streaming from Twitter...");
			StatusListener listener = new StatusListener() {
				public void onStatus(Status status) {
					readStatus(status);

					if (CREATEJSON) {
						try {

							FileWriter fw = new FileWriter(OUTPUTJSON, true);
							// Append to the file
							fw.write(TwitterObjectFactory.getRawJSON(status));
							fw.write(System.getProperty("line.separator"));

							// fw.write(System.lineSeparator()); // Only > Java
							// 7
							fw.close();

						} catch (IOException ioe) {
							System.err.println("IOException: " + ioe.getMessage());
						}
					}
				}

				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
				}

				public void onException(Exception ex) {
					ex.printStackTrace();
				}

				public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

				}

				public void onScrubGeo(long userId, long upToStatusId) {

				}

				public void onStallWarning(StallWarning warning) {

				}
			};
			/*
			 * Get tweets from
			 * https://stream.twitter.com/1.1/statuses/sample.json
			 */
			TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
			twitterStream.addListener(listener);
			// sample() method internally creates a thread which manipulates
			// TwitterStream and calls these adequate listener methods
			// continuously.
			twitterStream.sample();

		}

	}

	protected static void readStatus(Status status) {
		if (Top3App.DEBUG)
			System.out.println(status.getUser().getName() + " : " + status.getText());

		for (HashtagEntity ht : status.getHashtagEntities()) {

			String hashtag = ht.getText();
			String lang = status.getLang();
			if (Top3App.DEBUG)
				System.out.println("->" + lang + "," + hashtag);
			kafkaProducer.send(lang, hashtag);
		}
		
	}

	public static void printUsage() {
		System.out.println("Usage of startTwitterApp:");
		System.out
				.println("./startTwitterApp.sh mode apiKey apiSecret tokenValue tokenSecret kafkaBrokerURL Inputfile");
		System.exit(1);
	}
}
