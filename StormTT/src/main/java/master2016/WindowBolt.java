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
public class WindowBolt extends BaseRichBolt{
    
    private String window;
    private OutputCollector collector;
    private int opened; 
    
    public WindowBolt (String window){
    
        this.window = window; 
        this.opened = 0; 
        
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	declarer.declareStream(Topology.STREAMNAME,
				new Fields(Topology.HASHTAG_FIELDNAME, Topology.STATE_FIELDNAME));

	}

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

    }
    
    public boolean is_window_opened(){
        return (this.opened == 1);
    }

    public void execute(Tuple input) {
        
        String tupleHashtag = input.getValueByField(Topology.HASHTAG_FIELDNAME).toString();

        if (tupleHashtag.equals(window)) {

            if (Top3App.DEBUG)
                    System.out.println("WindowBolt => Hashtag: "+tupleHashtag + " window matchs");

            if (this.is_window_opened()){ 

                this.opened = 0; 

                //Send to the next only the hashtag
                collector.emit(input,new Values(tupleHashtag, "closed"));
                //Confirm received
                collector.ack(input);

            }   

            else{

                this.opened = 1; 

                //Send to the next only the hashtag
                collector.emit(input,new Values(tupleHashtag, "opened"));
                //Confirm received
                collector.ack(input);

            }
        }

            else{
            
                if (this.is_window_opened()){
                    
                    if (Top3App.DEBUG)
                    System.out.println("WindowBolt => Hashtag: "+tupleHashtag );
                
                    //Send to the next only the hashtag
                    collector.emit(input,new Values(tupleHashtag, "opened"));
                    //Confirm received
                    collector.ack(input);
                    
                }
            
            }
    
    }
    
}
