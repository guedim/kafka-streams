package com.guedim.kafkastreams.kafkastreams;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class WordCount {

  private static final String APPLICATION_ID = "wordcount-app";
  private static final String SERVER_CONFIG = "127.0.0.1:9092";
  private static final String RESET_CONFIG = "earliest";
  private static final String INPUT_TOPIC = "word-count-input";
  private static final String OUTPUT_TOPIC = "word-count-output";
  private static final String SPLIT_CHARACTER = " ";



  public static void wordCount() {
    Properties config = new Properties();
    config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_CONFIG);
    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, RESET_CONFIG);
    config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

    KStreamBuilder builder = new KStreamBuilder();
    // 1. Read data from kafka
    KStream<String, String> wordCountInput = builder.stream(INPUT_TOPIC);

    KTable<String, Long> wordCounts = wordCountInput
        // 2. Stream to lowerCase
        .mapValues(value -> value.toLowerCase())
        // 3. flatMap values by space
        .flatMapValues(
            lowerCasedTextLine -> Arrays.asList(lowerCasedTextLine.split(SPLIT_CHARACTER)))
        // 4. select key to apply a key
        .selectKey((ingonredKey, word) -> word)
        // 5. group by key before aggregation
        .groupByKey()
        // 6. count occurrences
        .count("Counts");

    wordCounts.to(Serdes.String(), Serdes.Long(), OUTPUT_TOPIC);

    KafkaStreams streams = new KafkaStreams(builder, config);
    streams.start();

    // Print the kafkaStream topology
    System.out.println(streams.toString());

    // Shutdown gracefully -- shutdown hook to correctly close the stream application
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

  }
}
