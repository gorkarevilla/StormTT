package master2016;

import java.util.ArrayList;
import java.util.List;

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
		Utils.sleep(45000);
		
		if (Top3App.DEBUG) System.out.println("Stoping...");
		topology.stop();
		if (Top3App.DEBUG) System.out.println("Stoped!");
		
	}
	
	

}
