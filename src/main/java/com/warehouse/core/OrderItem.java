package com.warehouse.core;

public class OrderItem {
  private final Long productId;
  private final Double quantity;

  public OrderItem(Long productId, Double quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public Long getProductId() {
    return productId;
  }

  public Double getQuantity() {
    return quantity;
  }
}
