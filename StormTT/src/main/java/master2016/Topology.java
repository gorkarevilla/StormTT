package master2016;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

/**
 * Defines de Topology to be used by Storm
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class Topology {

	public static final String STREAMNAME = "hashtagstream";
	public static final String LANGUAGE_FIELDNAME = "language";
	public static final String HASHTAG_FIELDNAME = "hashtag";

	// Only for local use
	private LocalCluster lcluster;

	// Configuration
	Config configuration;
	
	

	/**
	 * Builds the topology
	 * 
	 */
	public Topology() {
		start();
	}

	/**
	 * Start the Storm Topology
	 */
	public void start() {
		
		//Topology
		TopologyBuilder builder = new TopologyBuilder();
//		builder.setSpout("ReaderSpout", new ReaderSpout());
		builder.setBolt("filterBolt", new LangBolt(this.LANGUAGE_FIELDNAME, "es")).shuffleGrouping("ReaderSpout",
				this.STREAMNAME);
		
		//Configuration
		configuration = new Config();
		configuration.setDebug(true);
		
		
		//Create the cluster
		lcluster = new LocalCluster();
		lcluster.submitTopology("StormTopology",configuration, builder.createTopology());

	}

	/**
	 * Stop the Storm topology
	 */
	public void stop() {
		lcluster.shutdown();
	}

}
