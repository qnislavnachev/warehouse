package com.warehouse.core.exceptions;

public class OrderNotFoundException extends Exception {
  public final Long orderId;

  public OrderNotFoundException(Long orderId) {
    this.orderId = orderId;
  }
}
