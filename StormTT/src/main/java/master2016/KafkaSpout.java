/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

/**
 * First Spout in the Topology, will catch all the info
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class KafkaSpout extends BaseRichSpout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6295341977511499838L;

	private SpoutOutputCollector collector;

	//private KafkaConsumer kafkaConsumer = new KafkaConsumer(Top3App.kafkaBrokerURL);

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
	}

	public void nextTuple() {

		Values v = KafkaBrokerConsumer.getValues();
		
		List<String> valueList = Arrays.asList(v.toString().split(","));
		if(valueList.size()%2==0 && valueList.size()!=0) //Par
		{
			for (int i=0;i<valueList.size();i=i+2) {
				String lang = valueList.get(i).replace("[", ""); //Delete [ of the array
				String hashtag = valueList.get(i+1).replaceAll("]", ""); //Delete ] of the array
				
				collector.emit(Topology.STREAMNAME, new Values(lang,hashtag));
				if(Top3App.DEBUG) System.out.println("KafkaSpout=> "+lang+","+hashtag);
			}
			
		}
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.STREAMNAME,
				new Fields(Topology.LANGUAGE_FIELDNAME, Topology.HASHTAG_FIELDNAME));
	}

}
