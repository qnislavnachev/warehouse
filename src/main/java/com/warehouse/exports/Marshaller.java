package com.warehouse.exports;

import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.User;

public interface Marshaller {

  String marshall(User user);

  String marshall(Order order);

  String marshall(Product product);
}
