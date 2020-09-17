package com.warehouse.core.events;

import com.warehouse.core.Product;

import java.util.List;

public class ProductsLoadedEvent implements Event {
  public final List<Product> products;

  public ProductsLoadedEvent(List<Product> products) {
    this.products = products;
  }
}

