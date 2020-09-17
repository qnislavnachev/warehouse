package com.warehouse.adapter.dao.auditlog.orders;

import com.warehouse.adapter.dao.auditlog.LogAction;

public interface OrdersLogRepository {

  void log(Long orderId, LogAction action, String description);
}
