package com.warehouse.core;

import com.warehouse.adapter.security.AuthenticatedUser;

import java.util.List;

public class Order implements Resource {
  private final Long id;
  private final Long ownerId;
  private final List<OrderItem> orderItems;
  private final Double price;
  private boolean isPaid;

  public Order(Long id, Long ownerId, List<OrderItem> orderItems, Double price, boolean isPaid) {
    this.id = id;
    this.ownerId = ownerId;
    this.orderItems = orderItems;
    this.price = price;
    this.isPaid = isPaid;
  }

  public Order(Long ownerId, List<OrderItem> orderItems, Double price, boolean isPaid) {
    this.id = null;
    this.ownerId = ownerId;
    this.orderItems = orderItems;
    this.price = price;
    this.isPaid = isPaid;
  }

  public Long getId() {
    return id;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public Double getPrice() {
    return price;
  }

  public boolean isPaid() {
    return isPaid;
  }

  public void pay() {
    isPaid = true;
  }

  @Override
  public boolean isOwnedBy(AuthenticatedUser user) {
    return user.getId().equals(ownerId);
  }
}
