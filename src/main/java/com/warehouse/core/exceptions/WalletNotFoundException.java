package com.warehouse.core.exceptions;

public class WalletNotFoundException extends Exception {
  public final Long ownerId;

  public WalletNotFoundException(Long ownerId) {
    this.ownerId = ownerId;
  }
}
