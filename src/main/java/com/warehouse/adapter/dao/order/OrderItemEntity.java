package com.warehouse.adapter.dao.order;

import com.warehouse.core.OrderItem;

import javax.persistence.*;

@Entity
class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private OrderEntity order;

  private Long productId;
  private Double quantity;

  public OrderItemEntity() {
    this.id = null;
    this.order = null;
    this.productId = null;
    this.quantity = null;
  }

  private OrderItemEntity(Long productId, Double quantity, OrderEntity order) {
    this.id = null;
    this.order = order;
    this.productId = productId;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public OrderEntity getOrderEntity() {
    return order;
  }

  public Long getProductId() {
    return productId;
  }

  public Double getQuantity() {
    return quantity;
  }

  public static OrderItemEntity from(OrderItem orderItem, OrderEntity order) {
    return new OrderItemEntity(orderItem.getProductId(), orderItem.getQuantity(), order);
  }
}
