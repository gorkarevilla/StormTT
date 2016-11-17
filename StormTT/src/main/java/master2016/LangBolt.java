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
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Bolt Filter to catch only the languages needed.
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class LangBolt extends BaseRichBolt {

	/**
	 * AutoGenerated
	 */
	private static final long serialVersionUID = 5259021092740992581L;
	
	//Language to be filter
	private final String lang;
	
	//Collector
	private OutputCollector collector;

	public LangBolt(String lang) {
		this.lang = lang;
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	public void execute(Tuple input) {
		if (Top3App.DEBUG)
			System.out.println("LangBolt"+this.lang+"=> Received: " + input.getValues());

		String tupleLang = input.getValueByField(Topology.LANGUAGE_FIELDNAME).toString();
		String tupleHashtag = input.getValueByField(Topology.HASHTAG_FIELDNAME).toString();

		if (tupleLang.equals(lang)) {

                    if (Top3App.DEBUG)
                            System.out.println("LangBolt"+this.lang+"=> Language: " + tupleLang + " Hashtag: "+tupleHashtag);

                    //Send to the next only the hashtag
                    collector.emit(input,new Values(tupleHashtag));
                    //Confirm received
                    collector.ack(input);
                
                }
		
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// NO OUTPUTS

	}

}
