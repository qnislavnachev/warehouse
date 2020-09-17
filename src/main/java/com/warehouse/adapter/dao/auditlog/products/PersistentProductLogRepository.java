package com.warehouse.adapter.dao.auditlog.products;

import com.warehouse.adapter.dao.auditlog.LogAction;
import com.warehouse.core.date.DateTime;
import org.springframework.stereotype.Repository;

@Repository
class PersistentProductLogRepository implements ProductLogRepository {
  private final ProductsLogDatabase database;

  public PersistentProductLogRepository(ProductsLogDatabase database) {
    this.database = database;
  }

  @Override
  public void log(Long productId, LogAction action, String description) {
    ProductLogEntity entity = new ProductLogEntity(productId, action, description, DateTime.now());

    database.save(entity);
  }
}
