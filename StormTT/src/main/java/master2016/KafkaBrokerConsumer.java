package master2016;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.storm.tuple.Values;

public class KafkaBrokerConsumer {
	
	public Properties consumerProperties;
	
	//KafkaConsumer
	private static KafkaConsumer<String,String> kafkaConsumer;
	
	public KafkaBrokerConsumer() {
		configureConsumer();
		kafkaConsumer = new KafkaConsumer<String,String>(consumerProperties);
		
		kafkaConsumer.subscribe(Arrays.asList(Top3App.topicName));
	}
	
	public static Values getValues() {
		ConsumerRecords<String,String> records = kafkaConsumer.poll(10);
			Values lvalues = new Values();
			for(ConsumerRecord<String, String> record : records) {
				//System.out.println("Consumer: "+record.value());
				
				lvalues.add(record.value());
			}
		
		return lvalues;
		
	}
	
	public void configureConsumer() {
		consumerProperties = new Properties();
		consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,Top3App.kafkaBrokerURL);
		consumerProperties.put("group.id",Top3App.GROUP_ID);
		consumerProperties.put("enable.auto.commit","true");
		consumerProperties.put("auto.commit.intervals.ms","1000");
		consumerProperties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
		consumerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	}

}
