package com.warehouse.adapter.dao.warehouse;

import com.warehouse.core.OrderItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReservationRequest {
  private final Map<Long, Double> productQuantities;

  private ReservationRequest(Map<Long, Double> productQuantities) {
    this.productQuantities = productQuantities;
  }

  public Set<Long> getProductsIds() {
    return productQuantities.keySet();
  }

  public Double getQuantityOf(Long productId) {
    return productQuantities.getOrDefault(productId, 0.0);
  }

  public static ReservationRequest of(List<OrderItem> orderItems) {
    Map<Long, Double> productQuantities = new HashMap<>();

    for (OrderItem item : orderItems) {
      Double availableQuantity = productQuantities.getOrDefault(item.getProductId(), 0.0);

      productQuantities.put(item.getProductId(), item.getQuantity() + availableQuantity);
    }

    return new ReservationRequest(productQuantities);
  }
}
