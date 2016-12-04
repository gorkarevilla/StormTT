package master2016;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
* 
*
* @authors	Alvaro Feal;
* 			Gorka Revilla
* @version 	0.1
* @since   	07-11-2016 
*/
public class Top3App {

	//Debug mode for prints
	public static final Boolean DEBUG = false;
	
	
	//Default parameters values
	public static String langString = "es:casa";
	public static String kafkaBrokerURL = "localhost:9092";
	public static String topologyName = "StormTT";
	public static String folder = "output/";
	
	//Parameters for Kafka
	private static String topicName = "master2016";
	
	//Group ID
	public static final String GROUP_ID = "16";
	
	//Iteration Counter
	private static int counter = 0;
	
	//List with languages to be filter
	private static List<Lang> langList = new ArrayList<Lang>();
	
	
	//Properties for the KafkaConsumer
	private static Properties consumerProperties;
	
	//KafkaConsumer
	private static KafkaConsumer<String,String> kafkaConsumer;
	
	/**
	 * 
	 * @param args Parameters array: 
	 * 		0:	langList:		String with the list of languages (“lang” values) we are
	 * 							interested in and the associated special token. The list is in CSV format,
	 * 							example: en:house,pl:universidade,ar:carro,es:ordenador
	 * 
	 * 		1:	ZookeeperURL: 	String IP:port of the Zookeeper node.
	 * 
	 * 		2:	TopologyName: 	String identifying the topology in the Storm Cluster
	 * 
	 * 		3:	Folder: 		path to the folder used to store the output files (the path is
	 * 							relative to the filesystem of the node that will be used to run the Storm
	 * 							Supervisor)
	 * 
	 */
	public static void main(String[] args) {
	
		//Replace with Parameters
		if (args.length == 4) {
			langString = args[0];
			kafkaBrokerURL = args[1];
			topologyName = args[2];
			folder = args[3];
		}
	
		if (DEBUG) System.out.println("Parms: LangString: "+langString+" zookeeperURL: "+kafkaBrokerURL+" topologyName: "+
										topologyName + " folder: " + folder);
		
		//Create the List
		langToList(langString);
		
		//Create topology
		Topology topology = new Topology(topologyName,kafkaBrokerURL,langList,folder,GROUP_ID);
		
		configureConsumer();
		kafkaConsumer = new KafkaConsumer<String,String>(consumerProperties);
		
		try{
			
			kafkaConsumer.subscribe(Arrays.asList(topicName));
			while(true){
				ConsumerRecords<String,String> records = kafkaConsumer.poll(10);
				for(ConsumerRecord<String, String> record : records) {
					System.out.println("Language: "+record.value());
					
				}
			}
			
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			kafkaConsumer.close();
		}
		
		//Start the cluster
		//topology.startCluster();
		
		
		//Start local cluster
		topology.startLocalCluster();
		
		
		

	}
	
	/**
	 * 
	 * Create the langList with languages inside
	 * 
	 * 
	 * @param s with the CSV format:  lang1:window1,lang2:window2,lang3:window3
	 */
	private static void langToList(String s) {
		
		//Split all the languages in each cell
		String[] langs = s.split(",");
		
		//Split each language in id and window and insert in the list
		for(String l: langs) {
			String lang = l.split(":")[0];
			String window = l.split(":")[1];
			
			langList.add(new Lang(lang,window));
		}
		
		if (DEBUG) System.out.println("List: "+langList.toString());
		
	}
	
	
	

	/**
	 * Define properties for KafkaConsumer
	 */
	private static void configureConsumer() {
		consumerProperties = new Properties();
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaBrokerURL);
		consumerProperties.put("group.id",Top3App.GROUP_ID);
		consumerProperties.put("enable.auto.commit","true");
		consumerProperties.put("auto.commit.intervals.ms","1000");
		consumerProperties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}
	
	

}
