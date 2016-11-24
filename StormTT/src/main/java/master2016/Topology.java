package master2016;

import java.util.List;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;

/**
 * Defines the Topology to be used by Storm
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class Topology {

	// Name of the Topology
	public String topologyName = "StormTopology";
	public List<Lang> langList;
	public String folder;
	public String groupID;

	public static final String STREAMNAME = "hashtagstream";
	public static final String LANGUAGE_FIELDNAME = "language";
	public static final String HASHTAG_FIELDNAME = "hashtag";
	public static final String STATE_FIELDNAME = "state";

	public static final String TOP1HASHTAG_FIELDNAME = "top1hashtag";
	public static final String TOP1VALUE_FIELDNAME = "top1value";
	public static final String TOP2HASHTAG_FIELDNAME = "top2hashtag";
	public static final String TOP2VALUE_FIELDNAME = "top2value";
	public static final String TOP3HASHTAG_FIELDNAME = "top3hashtag";
	public static final String TOP3VALUE_FIELDNAME = "top3value";

	// Builder of the topology
	private TopologyBuilder builder;

	// Only for local use
	private LocalCluster lcluster;

	// Configuration
	private Config configuration;
	private Boolean DEBUGSTORM = false;
	private final int NWORKERS = 10;
	private final int MAXSPOUTPENDING = 5000;

	/**
	 * Builds the topology
	 * 
	 * @param folder
	 * @param langList
	 * 
	 */
	public Topology(String TopologyName, List<Lang> languageList, String f, String gid) {

		if (Top3App.DEBUG)
			System.out.println("Creating Topology.");

		this.topologyName = TopologyName;
		this.langList = languageList;
		this.folder = f;
		this.groupID = gid;

		// Topology
		builder = new TopologyBuilder();

		builder.setSpout("ReaderSpout", new ReaderSpout());

		// One bolt for each language
		for (Lang l : languageList) {
			builder.setBolt("LangBolt" + l.getId(), new LangBolt(l.getId())).shuffleGrouping("ReaderSpout",
					Topology.STREAMNAME);
			builder.setBolt("WindowBolt" + l.getId(), new WindowBolt(l.getId(), l.getWindow()))
					.shuffleGrouping("LangBolt" + l.getId(), Topology.STREAMNAME);
			builder.setBolt("ListBolt" + l.getId(), new ListBolt(l.getId())).shuffleGrouping("WindowBolt" + l.getId(),
					Topology.STREAMNAME);
			builder.setBolt("WritterBolt" + l.getId(), new WritterBolt(l.getId(), groupID))
					.shuffleGrouping("ListBolt" + l.getId(), Topology.STREAMNAME);
		}

		/*
		 * TODO Define topology
		 */

		// Configuration
		configuration = new Config();
		configuration.setDebug(this.DEBUGSTORM);
		configuration.setNumWorkers(this.NWORKERS);
		configuration.setMaxSpoutPending(this.MAXSPOUTPENDING);

	}

	/**
	 * Start the topology in a cluster
	 * 
	 */
	public void startCluster() {

		if (Top3App.DEBUG)
			System.out.println("Starting Cluster...");

		try {
			StormSubmitter.submitTopology(this.topologyName, configuration, builder.createTopology());
		} catch (AlreadyAliveException e) {
			e.printStackTrace();
		} catch (InvalidTopologyException e) {
			e.printStackTrace();
		} catch (AuthorizationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Start the Storm Topology when running locally
	 */
	public void startLocalCluster() {

		if (Top3App.DEBUG)
			System.out.println("Starting LocalCluster...");

		// Create the cluster
		lcluster = new LocalCluster();
		lcluster.submitTopology(this.topologyName, configuration, builder.createTopology());

	}

	/**
	 * Stop the Storm topology when running locally
	 */
	public void stop() {
		lcluster.shutdown();
	}

}
