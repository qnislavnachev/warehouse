package com.warehouse.adapter.dao.auditlog.orders;

import com.warehouse.adapter.dao.auditlog.LogAction;
import com.warehouse.core.date.DateTime;
import org.springframework.stereotype.Repository;

@Repository
class PersistentOrdersLogRepository implements OrdersLogRepository {
  private final OrdersLogDatabase database;

  public PersistentOrdersLogRepository(OrdersLogDatabase database) {
    this.database = database;
  }

  @Override
  public void log(Long orderId, LogAction action, String description) {
    OrderLogEntity log = new OrderLogEntity(orderId, action, description, DateTime.now());

    database.save(log);
  }
}
