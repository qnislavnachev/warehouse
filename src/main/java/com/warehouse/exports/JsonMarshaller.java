package com.warehouse.exports;

import com.google.gson.Gson;
import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.User;

public class JsonMarshaller implements Marshaller {
  private final Gson gson;

  public JsonMarshaller() {
    this.gson = new Gson();
  }

  @Override
  public String marshall(User user) {
    return gson.toJson(user);
  }

  @Override
  public String marshall(Order order) {
    return gson.toJson(order);
  }

  @Override
  public String marshall(Product product) {
    return gson.toJson(product);
  }
}
