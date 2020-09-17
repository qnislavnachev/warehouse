package com.warehouse.eventbus.listeners;

import com.warehouse.adapter.dao.auditlog.LogAction;
import com.warehouse.adapter.dao.auditlog.orders.OrdersLogRepository;
import com.warehouse.adapter.dao.auditlog.products.ProductLogRepository;
import com.warehouse.core.Order;
import com.warehouse.core.events.OrderCreatedEvent;
import com.warehouse.core.events.OrderPaidEvent;
import com.warehouse.core.events.ProductsLoadedEvent;
import org.springframework.stereotype.Component;

@Component
public class LoggingListener implements EventListener {
  private final OrdersLogRepository ordersLogs;
  private final ProductLogRepository productsLogs;

  public LoggingListener(OrdersLogRepository ordersLogs, ProductLogRepository productsLogs) {
    this.ordersLogs = ordersLogs;
    this.productsLogs = productsLogs;
  }

  public void handle(ProductsLoadedEvent event) {
    // TODO: Optimization for bulk logging should be added

    event.products.forEach(product -> {
              String description = String.format("Loading %s quantity of product with id %s", product.getQuantity(), product.getId());

              productsLogs.log(product.getId(), LogAction.CREATE, description);
            }
    );
  }

  public void handle(OrderCreatedEvent event) {
    Order order = event.order;

    String description = String.format("A new order was created with owner id %s", order.getOwnerId());
    ordersLogs.log(order.getId(), LogAction.CREATE, description);
  }

  public void handle(OrderPaidEvent event) {
    Order order = event.order;
    String description = String.format("Order with id %s was paid. The paid amount is %s", order.getId(), order.getPrice());

    ordersLogs.log(order.getId(), LogAction.UPDATE, description);
  }
}
