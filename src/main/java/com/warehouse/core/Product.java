package com.warehouse.core;

public class Product {

  private final Long id;
  private final String name;
  private final Double price;
  private final Double quantity;

  public Product(String name, Double price, Double quantity) {
    this.id = null;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  public Product(Long id, String name, Double price, Double quantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Double getPrice() {
    return price;
  }

  public Double getTotalPrice() {
    return price * quantity;
  }

  public Double getQuantity() {
    return quantity;
  }
}
