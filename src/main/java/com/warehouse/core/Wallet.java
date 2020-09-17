package com.warehouse.core;

public class Wallet {
  private final Long ownerId;
  private final Double balance;

  public Wallet(Long ownerId, Double balance) {
    this.ownerId = ownerId;
    this.balance = balance;
  }

  public Wallet(Double balance) {
    this.ownerId = null;
    this.balance = balance;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public Double getBalance() {
    return balance;
  }
}
