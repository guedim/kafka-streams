package com.guedim.kafkastreams.kafkastreams.bank;

import static com.guedim.kafkastreams.kafkastreams.bank.Properties.*;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.apache.kafka.connect.json.JsonSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

import com.fasterxml.jackson.databind.JsonNode;

public class BankBalanceExactlyOnceApp {

   
  public static void bankBalnceExactlyOnce() {
    
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, getServerConfig());
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, RESET_CONFIG);
    // we disable the cache to demonstrate all the "steps" involved in the transformation - not recommended in prod
    config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, NO_CACHE_CONFIG);
    // Exactly once processing!!
    config.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);

    // json Serde
    final Serializer<JsonNode> jsonSerializer = new JsonSerializer();
    final Deserializer<JsonNode> jsonDeserializer = new JsonDeserializer();
    final Serde<JsonNode> jsonSerde = Serdes.serdeFrom(jsonSerializer, jsonDeserializer);


    KStreamBuilder builder = new KStreamBuilder();
    KStream<String, JsonNode> bankTransactions = builder.stream(Serdes.String(), jsonSerde, INPUT_TOPIC);
   
    KTable<String, JsonNode> bankBalance =
        bankTransactions.groupByKey(Serdes.String(), jsonSerde).aggregate(() -> BalanceModel.initialBalance(),
            (key, transaction, balance) -> BalanceModel.newBalance(transaction, balance), jsonSerde, "bank-balance-agg");

    bankBalance.to(Serdes.String(), jsonSerde, OUTPUT_TOPIC);

    KafkaStreams streams = new KafkaStreams(builder, config);
    streams.cleanUp();
    streams.start();

    // print the topology
    System.out.println(streams.toString());

    // shutdown hook to correctly close the streams application
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
  } 
}