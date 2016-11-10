package master2016;

import org.apache.storm.utils.Utils;

/**
*
*
* @authors	Alvaro Feal;
* 			Gorka Revilla
* @version 	0.1
* @since   	07-11-2016 
*/
public class TopologyTest {

	private static Topology topology;
	
	public static void main(String[] args) {
		if (Top3App.DEBUG) System.out.println("");
		topology = new Topology();

		topology.start();
		
		
		Utils.sleep(30000);
		
		topology.stop();
		
	}
	
	

}
