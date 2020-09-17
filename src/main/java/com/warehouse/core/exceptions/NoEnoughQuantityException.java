package com.warehouse.core.exceptions;

public class NoEnoughQuantityException extends Exception {
  public final Long productId;
  public final Double availableQuantity;


  public NoEnoughQuantityException(Long productId, Double availableQuantity) {
    this.productId = productId;
    this.availableQuantity = availableQuantity;
  }
}
