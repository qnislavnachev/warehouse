package com.warehouse.core.events;

import com.warehouse.core.Order;

public class OrderPaidEvent implements Event {
  public final Order order;

  public OrderPaidEvent(Order order) {
    this.order = order;
  }
}
