package com.warehouse.adapter.dao.auditlog.products;

import com.warehouse.adapter.dao.auditlog.LogAction;

public interface ProductLogRepository {

  void log(Long productId, LogAction action, String description);
}
