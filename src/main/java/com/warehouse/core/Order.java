package com.warehouse.core;

import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.exports.Exportable;
import com.warehouse.exports.Marshaller;

import java.util.List;
import java.util.stream.Collectors;

public class Order implements Resource, Exportable, Duplicator<Order> {
  private final Long id;
  private final Long ownerId;
  private final List<OrderItem> orderItems;
  private final Double price;
  private final boolean isPaid;

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

  @Override
  public boolean isOwnedBy(AuthenticatedUser user) {
    return user.getId().equals(ownerId);
  }

  @Override
  public String accept(Marshaller marshaller) {
    return marshaller.marshall(this);
  }

  @Override
  public Order duplicate() {
    List<OrderItem> clonedItems = this.orderItems.stream().map(OrderItem::duplicate).collect(Collectors.toList());

    return new Order(this.id, this.ownerId, clonedItems, this.price, this.isPaid);
  }
}
