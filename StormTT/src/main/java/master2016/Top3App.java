package master2016;


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
	private static final Boolean DEBUG = true;
	
	
	//Default parameters values
	private static String langList = "es,casa";
	private static String zookeeperURL = "localhost:2181";
	private static String topologyName = "StormTT";
	private static String folder = "output/";
	
	//Group ID
	private static final String GROUP_ID = "GOAL";
	
	//Iteration Counter
	private static int counter = 0;
	
	
	
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
			langList = args[0];
			zookeeperURL = args[1];
			topologyName = args[2];
			folder = args[3];
		}
	
		if (DEBUG) System.out.println("Parms: LangList: "+langList+" zookeeperURL: "+zookeeperURL+" topologyName: "+
										topologyName + " folder: " + folder);
		
		

	}
	
	

}
