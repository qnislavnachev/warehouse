package com.warehouse.core.exceptions;

public class ProductNotFoundException extends Exception {
  public final Long productId;

  public ProductNotFoundException(Long productId) {
    this.productId = productId;
  }
}
