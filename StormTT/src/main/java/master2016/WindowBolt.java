/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.util.Map;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 *
 * @author alvarofeal
 */
public class WindowBolt extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8262948702119426728L;
	
	private String language;
	private String window;
	private OutputCollector collector;
	private boolean isOpen;

	public WindowBolt(String lang, String window) {

		this.language = lang;
		this.window = window;
		this.isOpen = false;
		
		if(Top3App.INFO)
			System.out.println("Window "+language+" is Closed");

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.STREAMNAME, new Fields(Topology.HASHTAG_FIELDNAME, Topology.STATE_FIELDNAME));

	}

	@SuppressWarnings("rawtypes")
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	/**
	 * 
	 * 
	 * Will Emit: closed or opened to next bolt
	 * 
	 * 
	 */
	public void execute(Tuple input) {

		String tupleHashtag = input.getValueByField(Topology.HASHTAG_FIELDNAME).toString();

		if (tupleHashtag.equals(window)) {

			if (Top3App.DEBUG)
				System.out.println("WindowBolt" + this.language + "=> Hashtag: " + tupleHashtag + " window matchs");

			if (this.isOpen) {

				this.isOpen = false;

				// Send to the next only the hashtag
				collector.emit(Topology.STREAMNAME, new Values(tupleHashtag, "closed"));
				// Confirm received
				collector.ack(input);

				if(Top3App.INFO)
					System.out.println("Window "+language+" is Closed");
			}

			else {

				this.isOpen = true;

				// Send to the next only the hashtag
				collector.emit(Topology.STREAMNAME, new Values(tupleHashtag, "opened"));
				// Confirm received
				collector.ack(input);
				
				if(Top3App.INFO)
					System.out.println("Window "+language+" is Opened");
			}
		} else {

			if (this.isOpen) {

				if (Top3App.DEBUG)
					System.out.println("WindowBolt" + this.language + "=> Hashtag: " + tupleHashtag);

				// Send to the next only the hashtag
				collector.emit(Topology.STREAMNAME, new Values(tupleHashtag, "opened"));
				// Confirm received
				collector.ack(input);

			}

		}

	}

}
