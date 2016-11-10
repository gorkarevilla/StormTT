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
import java.util.Random;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

public class ReaderSpout {
    
    private SpoutOutputCollector collector;
    public static final String LANGUAGE_STREAMNAME = "languagestream";
    public static final String LANGUAGE_FIELDNAME = "language"; 
    public static final String WINDOW_FIELDNAME = "window"; 

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
            this.collector=collector;
    }

    public void nextTuple() {
            // CREATE EVENTS FOR 1000 ROOMS WITH TEMPERATURE BETWEEN 0 AND 199 C
            Values values = new Values("language"+"es");
            collector.emit(LANGUAGE_STREAMNAME,values);
            System.out.println("Emitting: " + values);
            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
            declarer.declareStream(LANGUAGE_STREAMNAME, new Fields(LANGUAGE_FIELDNAME, WINDOW_FIELDNAME));
    }
    
}
