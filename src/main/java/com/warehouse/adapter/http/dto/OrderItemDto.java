package com.warehouse.adapter.http.dto;

import com.warehouse.core.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderItemDto {
  public Long productId;
  public Double quantity;

  public OrderItemDto() {
    this.productId = null;
    this.quantity = 0.0;
  }

  public OrderItemDto(Long productId, Double quantity) {
    this.productId = productId;
    this.quantity = quantity;
  }

  public static List<OrderItemDto> adapt(List<OrderItem> orderItems) {
    return orderItems.stream()
            .map(it -> new OrderItemDto(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());
  }
}
