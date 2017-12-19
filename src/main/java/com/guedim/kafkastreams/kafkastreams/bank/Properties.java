package com.guedim.kafkastreams.kafkastreams.bank;

public final class Properties {

  public static final String APPLICATION_ID = "bank-balance-application";
  public static final String RESET_CONFIG = "earliest";
  public static final String SERVER_CONFIG_HOST = "127.0.0.1";
  public static final String SERVER_CONFIG_PORT = "9092";
  public static final String ADVERTISED_HOST = "ADVERTISED_HOST";
  public static final String INPUT_TOPIC = "bank-transactions";
  public static final String OUTPUT_TOPIC = "bank-balance-exactly-once";
  public static final String NO_CACHE_CONFIG = "0";

  public static String getServerConfig() {
    return kafkaHostName() + ":" + SERVER_CONFIG_PORT;
  }
  
  private static String kafkaHostName() {
    String hostProperty = System.getProperty(ADVERTISED_HOST);
    String hostEnvironm = System.getenv(ADVERTISED_HOST);
    String host = hostProperty != null ? hostProperty : hostEnvironm != null ? hostEnvironm : SERVER_CONFIG_HOST;
    System.out.println("Host Property:" + hostProperty);
    System.out.println("Host Environm:" + hostEnvironm);
    System.out.println("Advertised host:" + host);
    return host;
  }
}
