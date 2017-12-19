package com.guedim.kafkastreams.kafkastreams.bank;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import static com.guedim.kafkastreams.kafkastreams.bank.Properties.*;

public class BankTransactionsProducer {
  public static void main(String[] args) {

    Properties properties = new Properties();
    // kafka bootstrap server
    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getServerConfig());
    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    // producer acks
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // strongest producing guarantee
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, "3");
    properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, "1");
    // leverage idempotent producer from Kafka 0.11 !
    properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // ensure we don't push duplicates

    Producer<String, String> producer = new KafkaProducer<>(properties);

    int i = 0;
    while (true) {
      System.out.println("Producing batch: " + i);
      try {
        producer.send(ProducerRecordModel.newRandomTransaction("john"));
        Thread.sleep(100);
        producer.send(ProducerRecordModel.newRandomTransaction("stephane"));
        Thread.sleep(100);
        producer.send(ProducerRecordModel.newRandomTransaction("alice"));
        Thread.sleep(100);
        i += 1;
      } catch (InterruptedException e) {
        break;
      }
    }
    producer.close();
  }
}
