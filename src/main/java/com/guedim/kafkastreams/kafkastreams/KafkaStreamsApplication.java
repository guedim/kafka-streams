package com.guedim.kafkastreams.kafkastreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.guedim.kafkastreams.kafkastreams.bank.BankBalanceExactlyOnceApp;

@SpringBootApplication
public class KafkaStreamsApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaStreamsApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void readStreams() {
    BankBalanceExactlyOnceApp.bankBalnceExactlyOnce();
  }
}
