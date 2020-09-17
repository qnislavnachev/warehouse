package com.warehouse.adapter.http.dto;

public class NewProductDto {
  public String name;
  public Double price;
  public Double quantity;

  public NewProductDto() {
    this.name = "";
    this.price = 0.0;
    this.quantity = 0.0;
  }
}
