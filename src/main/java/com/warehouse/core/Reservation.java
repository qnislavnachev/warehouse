package com.warehouse.core;

import java.util.ArrayList;
import java.util.List;

public class Reservation {
  private final List<Product> products;

  public Reservation(List<Product> products) {
    this.products = products;
  }

  public Reservation() {
    this.products = new ArrayList<>();
  }

  public Double getTotalPrice() {
    Double price = 0.0;

    for (Product product : products) {
      price += product.getTotalPrice();
    }

    return price;
  }
}
