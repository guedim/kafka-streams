package com.guedim.kafkastreams.kafkastreams;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class FavouriteColor {

	private static final String APPLICATION_ID = "favourite-color-app";
	private static final String SERVER_CONFIG = "127.0.0.1:9092";
	private static final String RESET_CONFIG = "earliest";
	private static final String INPUT_TOPIC = "fav-color-input";
	private static final String MIDDLE_TOPIC = "fav-color-middle";
	private static final String OUTPUT_TOPIC = "fav-color-output";
	private static final String SPLIT_CHARACTER = ",";

	public static void favourtiteColor() {
		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_ID);
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER_CONFIG);
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, RESET_CONFIG);
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		KStreamBuilder builder = new KStreamBuilder();

		// 1. Read data from kafka
		KStream<String, String> favColorInput = builder.stream(INPUT_TOPIC);
		// 2. Select key
		KStream<String, String> favColorOutput = favColorInput
				.selectKey((key, value) -> value.toLowerCase().substring(0, value.indexOf(SPLIT_CHARACTER)))
				// 3. set value
				.mapValues(value -> value.toLowerCase().substring(value.indexOf(SPLIT_CHARACTER) + 1, value.length()))
				// 4. filter bad values
				.filter((key, value) -> Arrays.asList("blue", "red", "green").contains(value.toLowerCase()));
		// 5. Write intermediate data to kafka
		favColorOutput.to(Serdes.String(), Serdes.String(), MIDDLE_TOPIC);

		// 6. Read data as Ktable
		KTable<String, String> kTableColorInput = builder.table(MIDDLE_TOPIC);
		KTable<String, Long> kTableColorOutput = kTableColorInput
				.groupBy((user, color) -> new KeyValue<>(color, color))
				.count("FavColor");
		kTableColorOutput.to(Serdes.String(), Serdes.Long(), OUTPUT_TOPIC);

		KafkaStreams streams = new KafkaStreams(builder, config);
		streams.start();

		// Print the kafkaStream topology
		System.out.println(streams.toString());

		// Shutdown gracefully -- shutdown hook to correctly close the stream
		// application
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}

}
