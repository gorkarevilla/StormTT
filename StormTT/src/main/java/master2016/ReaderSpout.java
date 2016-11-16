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
import java.lang.IllegalArgumentException;
import static master2016.Top3App.DEBUG;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class ReaderSpout extends BaseRichSpout{
    
    private SpoutOutputCollector collector;
    public static final String LANGUAGE_STREAMNAME = "languagestream";
    public static final String LANGUAGE_FIELDNAME = "language"; 
    public static final String WINDOW_FIELDNAME = "window"; 

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.collector=collector;
    }

    public void nextTuple() {
            
            Values values = new Values("language"+"es"+"ordenador");
            collector.emit(LANGUAGE_STREAMNAME,values);
            if (DEBUG) {
                System.out.println("Emitting: " + values);
            }
           
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declareStream(LANGUAGE_STREAMNAME, new Fields(LANGUAGE_FIELDNAME, WINDOW_FIELDNAME));
    }
    
}
