package com.warehouse.adapter.http.dto;

import com.warehouse.core.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDto {
  public Long id;
  public String name;
  public Double price;
  public Double quantity;

  public ProductDto() {
    this.id = null;
    this.name = "";
    this.price = 0.0;
    this.quantity = 0.0;
  }

  public ProductDto(Long id, String name, Double price, Double quantity) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  public static ProductDto adapt(Product product) {
    return new ProductDto(product.getId(), product.getName(), product.getPrice(), product.getQuantity());
  }

  public static List<ProductDto> adapt(List<Product> products) {
    return products.stream().map(it -> adapt(it)).collect(Collectors.toList());
  }
}
