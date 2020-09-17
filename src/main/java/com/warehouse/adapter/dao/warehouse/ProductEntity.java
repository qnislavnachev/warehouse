package com.warehouse.adapter.dao.warehouse;

import com.warehouse.core.Product;

import javax.persistence.*;

@Entity
class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;
  private Double price;
  private Double quantity;
  private Double blockedQuantity;

  protected ProductEntity() {
    this.id = null;
    this.name = null;
    this.price = null;
    this.quantity = null;
    this.blockedQuantity = null;
  }

  protected ProductEntity(Long id, String name, Double price, Double quantity, Double blockedQuantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
    this.blockedQuantity = blockedQuantity;
  }

  Long getId() {
    return id;
  }

  String getName() {
    return name;
  }

  Double getPrice() {
    return price;
  }

  Double getQuantity() {
    return quantity;
  }

  Double getBlockedQuantity() {
    return blockedQuantity;
  }

  void addQuantity(Double quantity) {
    this.quantity += quantity;
  }

  boolean reserve(Double quantity) {
    if (this.quantity >= quantity) {
      this.blockedQuantity += quantity;
      this.quantity -= quantity;

      return true;
    }

    return false;
  }

  boolean sellStock(Double quantity) {
    Double actualQuantity = this.quantity - this.blockedQuantity;

    if (actualQuantity > 0 && actualQuantity >= quantity) {
      this.quantity -= quantity;
      return true;
    }

    return false;
  }

  void sellReservedStock(Double quantity) {
    this.blockedQuantity -= quantity;
  }

  static ProductEntity from(Product product) {
    return new ProductEntity(null, product.getName(), product.getPrice(), product.getQuantity(), 0.0);
  }
}
