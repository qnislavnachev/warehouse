package com.warehouse.core.events;

import com.warehouse.core.Order;

public class OrderCreatedEvent implements Event {
  public final Order order;

  public OrderCreatedEvent(Order order) {
    this.order = order;
  }
}
