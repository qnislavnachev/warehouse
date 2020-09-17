package com.warehouse.adapter.http.dto;

import java.util.Collections;
import java.util.List;

public class CreateOrderRequest {
  public Long ownerId;
  public List<OrderItemDto> orderItems;

  public CreateOrderRequest() {
    this.ownerId = null;
    this.orderItems = Collections.emptyList();
  }

  public CreateOrderRequest(Long ownerId, List<OrderItemDto> orderItems) {
    this.ownerId = ownerId;
    this.orderItems = orderItems;
  }
}
