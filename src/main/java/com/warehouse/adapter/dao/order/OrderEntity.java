package com.warehouse.adapter.dao.order;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long ownerId;
  private Double price;
  private boolean isPaid;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItemEntity> orderItems;

  public OrderEntity() {
    this.id = null;
    this.ownerId = null;
    this.price = null;
    this.orderItems = Collections.emptyList();
    this.isPaid = false;
  }

  public OrderEntity(Long ownerId, Double price) {
    this.id = null;
    this.ownerId = ownerId;
    this.price = price;
    this.orderItems = new ArrayList<>();
    this.isPaid = false;
  }

  public Long getId() {
    return id;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public Double getPrice() {
    return price;
  }

  public List<OrderItemEntity> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItemEntity> orderItems) {
    this.orderItems = orderItems;
  }

  public boolean isPaid() {
    return isPaid;
  }

  public void markAsPaid() {
    this.isPaid = true;
  }
}
