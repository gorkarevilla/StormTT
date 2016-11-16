/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

/**
 *
 * @author alvarofeal
 */

import java.util.Map;
import static master2016.Top3App.DEBUG;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

public class LangBolt extends BaseRichBolt{
    
        private final String fieldname;
	private final String lang;
        
	public LangBolt (String fieldname, String lang){
		this.fieldname = fieldname;
		this.lang = lang;
	}
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		//NOTHING TO PREPARE 
		
	}

	public void execute(Tuple input) {
		if (DEBUG) {
                    System.out.println("Received: " + input.getValues());
                }
		String valueByField = input.getValueByField(fieldname).toString();
		
            if(valueByField.equals(lang)) {
            
            // DO WORK
            
            } 
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// NO OUTPUTS
		
	}
    
}
