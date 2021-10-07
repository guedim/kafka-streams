package com.guedim.kafkastreams.kafkastreams.bank;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class BalanceModel {

  public static JsonNode newBalance(JsonNode transaction, JsonNode balance) {
    // create a new balance json object
    ObjectNode newBalance = JsonNodeFactory.instance.objectNode();
    newBalance.put("count", balance.get("count").asInt() + 1);
    newBalance.put("balance", balance.get("balance").asInt() + transaction.get("amount").asInt());
    newBalance.put("time", newBalanceInstant(transaction, balance));
    return newBalance;
  }

  public static ObjectNode initialBalance() {
    // create the initial json object for balances
    ObjectNode initialBalance = JsonNodeFactory.instance.objectNode();
    initialBalance.put("count", 0);
    initialBalance.put("balance", 0);
    initialBalance.put("time", Instant.ofEpochMilli(0L).toString());
    return initialBalance;
  }

  private static String newBalanceInstant(JsonNode transaction, JsonNode balance) {
    Long balanceEpoch = Instant.parse(balance.get("time").asText()).toEpochMilli();
    Long transactionEpoch = Instant.parse(transaction.get("time").asText()).toEpochMilli();
    return Instant.ofEpochMilli(Math.max(balanceEpoch, transactionEpoch)).toString();
  }
}
