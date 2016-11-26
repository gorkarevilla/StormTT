/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * Bolt to control the lists and top3.
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class ListBolt extends BaseRichBolt {

	private String language;
	private OutputCollector collector;

	private Map<String, Integer> hashtagList;

	public ListBolt(String lang) {

		this.language = lang;

		hashtagList = new HashMap<String, Integer>();

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream(Topology.STREAMNAME,
				new Fields(Topology.TOP1HASHTAG_FIELDNAME, Topology.TOP1VALUE_FIELDNAME, Topology.TOP2HASHTAG_FIELDNAME,
						Topology.TOP2VALUE_FIELDNAME, Topology.TOP3HASHTAG_FIELDNAME, Topology.TOP3VALUE_FIELDNAME));

	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	/**
	 * 
	 * Will add to the list
	 * 
         * @param input
	 */
	public void execute(Tuple input) {

		
		if (Top3App.DEBUG)
			System.out.println("ListBolt" + this.language + "HelloList");
		
		String hashtag = input.getValueByField(Topology.HASHTAG_FIELDNAME).toString();
		boolean isOpen = (input.getValueByField(Topology.STATE_FIELDNAME).toString()).equals("opened");

		if (isOpen) {

			addToList(hashtag);

			if (Top3App.DEBUG)
				System.out.println("ListBolt" + this.language + "=> Hashtag: " + hashtag + " add to List");

		} else { // IF is Closed send to next Spout the top3 and clear the list

			String[][] top3 = getTop3Array();

			// Send to the next only the hashtags and values
			collector.emit(Topology.STREAMNAME, new Values(top3[0][0], top3[0][1], top3[1][0], top3[1][1], top3[2][0], top3[2][1]));

			// Confirm received
			collector.ack(input);
			
			
			this.hashtagList.clear();

		}
	}

	/**
	 * Add hashtag to list or increase the value
	 * 
	 * @param hashtag
	 */
	private void addToList(String hashtag) {

		// If contains the hashtag, add 1
		if (hashtagList.containsKey(hashtag)) {
			hashtagList.put(hashtag, hashtagList.get(hashtag) + 1);
		} else { // Add with 1
			hashtagList.put(hashtag, 1);
		}

	}

	/**
	 * 
	 * @return array 3 rows and 2 cols with the top3 Top1 = array[0][x] Top2 =
	 *         array[1][x] Top3 = array[2][x]
	 */
	private String[][] getTop3Array() {
		String[][] top3 = new String[3][2];
                ValueComparator bvc =  new ValueComparator(this.hashtagList);
                TreeMap<String,Integer> sorted_hashtag = new TreeMap<String,Integer>(bvc);
                
                sorted_hashtag.putAll(this.hashtagList); 
                Set set = sorted_hashtag.entrySet();
                Iterator i = set.iterator();
                for (int c = 0; c<3; c++) {                   
                   
                   if (i.hasNext() == false){
                       top3[c][0] = "null";
                       top3[c][1] = "null";
                   
                   }
                   else {
                       Map.Entry me = (Map.Entry)i.next();
                       top3[c][0] = me.getKey().toString();
                       top3[c][1] = me.getValue().toString();
                   }
                }

		// TODO

		if (Top3App.DEBUG) {
			System.out.println("ListBolt" + this.language + "=> Top1: " + top3[0][0] + ":" + top3[0][1]);
			System.out.println("ListBolt" + this.language + "=> Top2: " + top3[1][0] + ":" + top3[1][1]);
			System.out.println("ListBolt" + this.language + "=> Top3: " + top3[2][0] + ":" + top3[2][1]);
		}

		return top3;
	}

}
