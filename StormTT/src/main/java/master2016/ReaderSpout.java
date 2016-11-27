/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;

/**
 * First Spout in the Topology, will catch all the info
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class ReaderSpout extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6295341977511499838L;
	
	private SpoutOutputCollector collector;

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {
		//Only for testing, Remove in the future
		collector.emit(Topology.STREAMNAME, TopologyTest.generateRandomInputs());

		//TODO
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.STREAMNAME,
				new Fields(Topology.LANGUAGE_FIELDNAME, Topology.HASHTAG_FIELDNAME));
	}

}
