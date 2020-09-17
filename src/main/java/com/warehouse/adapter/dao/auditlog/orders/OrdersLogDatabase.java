package com.warehouse.adapter.dao.auditlog.orders;

import org.springframework.data.repository.CrudRepository;

interface OrdersLogDatabase extends CrudRepository<OrderLogEntity, Long> {
}
