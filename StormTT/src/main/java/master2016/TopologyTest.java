package master2016;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import master2016.Lang;
import master2016.Top3App;
import master2016.Topology;

import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;

/**
* Test topology Locally
*
* @authors	Alvaro Feal;
* 			Gorka Revilla
* @version 	0.1
* @since   	07-11-2016 
*/
public class TopologyTest {

	private static Topology topology;
	
	
	
	//Array with langs to choose randomly
	private static String[] langs = {"es","en","ar"};
	
	//Ten words to test randomly
	private static String[] hashtagsES = {"hola","que","si","adios","casa","piso","coche","dormir","bien","mal"};
	private static String[] hashtagsEN = {"hello","bye","yes","no","home","sleep","try","best","good","bad"};
	private static String[] hashtagsAR = {"carro","hogar","pive","chao","chevere","boludo","mate","coger","che","linda"};

	
	public static void main(String[] args) {
		
		//Create a list
		List<Lang> langList = new ArrayList<Lang>();
		
		// Lang, Window
		langList.add(new Lang("es","casa"));
		langList.add(new Lang("en","home"));
		langList.add(new Lang("ar","hogar"));
		
		
		if (Top3App.DEBUG) System.out.println("Starting...");
		topology = new Topology("StormTopology",langList,"output/");
		
		topology.startLocalCluster();
		
		if (Top3App.DEBUG) System.out.println("Started!");
		
		if (Top3App.DEBUG) System.out.println("Sending Messages.");
		
		/*
		for (int i=0;i<100;++i){
			generateRandomInputs();
			Utils.sleep(500);
		}
		*/
		
		Utils.sleep(30000);
		
		if (Top3App.DEBUG) System.out.println("Stoping...");
		topology.stop();
		if (Top3App.DEBUG) System.out.println("Stoped!");
		
	}
	
	
	/**
	 * lang
	 * hashtag
	 * 
	 * @return
	 */
	public static Values generateRandomInputs(){
				
		String lang = langs[new Random().nextInt(3)];
		String hashtag = "";
		
		if(lang.equals("es")){
			hashtag = hashtagsES[new Random().nextInt(10)];
		}else if(lang.equals("en")){
			hashtag = hashtagsEN[new Random().nextInt(10)];
		}else if(lang.equals("ar")){
			hashtag = hashtagsAR[new Random().nextInt(10)];
		}else {
			if(Top3App.DEBUG) System.err.println("Out of Range: ");
		}
		
		if(Top3App.DEBUG) System.out.println("Sending=> Lang: "+lang+" Hashtag: "+hashtag);
		
		return new Values(lang, hashtag);
	}
	
	

}
