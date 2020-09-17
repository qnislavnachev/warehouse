package com.warehouse.adapter.http.dto;

import com.warehouse.core.Order;

import java.util.Collections;
import java.util.List;

public class OrderDto {
  public Long id;
  public Long ownerId;
  public Double price;
  public List<OrderItemDto> orderItems;
  public boolean isPaid;

  public OrderDto() {
    this.id = null;
    this.ownerId = 0L;
    this.price = 0.0;
    this.orderItems = Collections.emptyList();
    this.isPaid = false;
  }

  public OrderDto(Long id, Long ownerId, Double price, List<OrderItemDto> orderItems, boolean isPaid) {
    this.id = id;
    this.ownerId = ownerId;
    this.price = price;
    this.orderItems = orderItems;
    this.isPaid = isPaid;
  }

  public static OrderDto adapt(Order order) {
    return new OrderDto(order.getId(), order.getOwnerId(), order.getPrice(), OrderItemDto.adapt(order.getOrderItems()), order.isPaid());
  }
}
