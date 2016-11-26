/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Bolt Writter to write each file.
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class WritterBolt extends BaseRichBolt {

	// Language to be filter
	private final String lang;
	private String groupID;
	private String filename;
	private int counter;

	// Collector
	private OutputCollector collector;

	public WritterBolt(String lang, String gid) {
		this.lang = lang;
		this.groupID = gid;

		this.filename = this.lang + "_" + this.groupID + ".log";
		this.counter = 0;
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	public void execute(Tuple input) {
		 if (Top3App.DEBUG)
		 System.out.println("WritterBolt" + this.lang + "=> Received: " +
		 input.getValues());

		++counter;

		String top1Hashtag = input.getValueByField(Topology.TOP1HASHTAG_FIELDNAME).toString();
		String top1Value = input.getValueByField(Topology.TOP1VALUE_FIELDNAME).toString();
		String top2Hashtag = input.getValueByField(Topology.TOP2HASHTAG_FIELDNAME).toString();
		String top2Value = input.getValueByField(Topology.TOP2VALUE_FIELDNAME).toString();
		String top3Hashtag = input.getValueByField(Topology.TOP3HASHTAG_FIELDNAME).toString();
		String top3Value = input.getValueByField(Topology.TOP3VALUE_FIELDNAME).toString();

		// TODO WRITE TO FILE
		try {

			FileWriter fw = new FileWriter(filename, true); 
			
			//Append to the file
			fw.write(this.counter + "," + this.lang + "," 
					+ top1Hashtag + "," + top1Value + "," 
					+ top2Hashtag + "," + top2Value + ","
					+ top3Hashtag + "," + top3Value); 
			fw.close();
			
		} catch (IOException ioe) {
			System.err.println("IOException: " + ioe.getMessage());
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.STREAMNAME, new Fields(Topology.HASHTAG_FIELDNAME));
	}

}
