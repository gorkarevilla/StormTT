/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master2016;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.storm.tuple.Values;

/**
 * 
 *
 * @authors Alvaro Feal; Gorka Revilla
 * @version 0.1
 * @since 07-11-2016
 */
public class KafkaBrokerProducer {

	public Properties producerProperties;

	// KafkaConsumer
	private static KafkaProducer<String, String> kafkaProducer;
	private final String topic = "master2016";
	private final int partition = 0;

	public KafkaBrokerProducer() {

		configureProducer();
		kafkaProducer = new KafkaProducer<String, String>(producerProperties);

	}

	public void send(Values values) {
		kafkaProducer.send(new ProducerRecord<String, String>(topic, values.get(0).toString()+","+
				values.get(1).toString()));

	}

	private void configureProducer() {
		producerProperties = new Properties();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Top3App.kafkaBrokerURL);
		producerProperties.put("group.id", Top3App.GROUP_ID);
		producerProperties.put("acks", "all");
		producerProperties.put("retries", 0);
		producerProperties.put("batch.size", 16384);
		producerProperties.put("buffer.memory", 33554432);
		producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		producerProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
	}

}
