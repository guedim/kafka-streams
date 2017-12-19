package com.guedim.kafkastreams.kafkastreams.bank;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.ProducerRecord;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class ProducerRecordModel {

  private static final String INPUT_TOPIC = "bank-transactions";
  
  public static ProducerRecord<String, String> newRandomTransaction(String name) {
    // creates an empty json {}
    ObjectNode transaction = JsonNodeFactory.instance.objectNode();
    // { "amount" : 46 } (46 is a random number between 0 and 100 excluded)
    Integer amount = ThreadLocalRandom.current().nextInt(0, 100);
    // Instant.now() is to get the current time using Java 8
    Instant now = Instant.now();
    // we write the data to the json document
    transaction.put("name", name);
    transaction.put("amount", amount);
    transaction.put("time", now.toString());
    return new ProducerRecord<>(INPUT_TOPIC, name, transaction.toString());
  }
}
