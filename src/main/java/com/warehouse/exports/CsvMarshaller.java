package com.warehouse.exports;

import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.User;

public class CsvMarshaller implements Marshaller {

  @Override
  public String marshall(User user) {
    StringBuilder builder = new StringBuilder();

    builder.append(user.getId()).append(",");
    builder.append(user.getEmail()).append(",");
    builder.append(user.getName()).append(",");
    builder.append("\n");

    return builder.toString();
  }

  @Override
  public String marshall(Order order) {
    StringBuilder builder = new StringBuilder();

    builder.append(order.getId()).append(",");
    builder.append(order.getOwnerId()).append(",");
    builder.append(order.getPrice()).append(",");
    builder.append(order.isPaid()).append(",");
    builder.append("\n");

    return builder.toString();
  }

  @Override
  public String marshall(Product product) {
    StringBuilder builder = new StringBuilder();

    builder.append(product.getId()).append(",");
    builder.append(product.getName()).append(",");
    builder.append(product.getQuantity()).append(",");
    builder.append(product.getPrice()).append(",");
    builder.append(product.getTotalPrice()).append(",");
    builder.append("\n");

    return builder.toString();
  }
}
