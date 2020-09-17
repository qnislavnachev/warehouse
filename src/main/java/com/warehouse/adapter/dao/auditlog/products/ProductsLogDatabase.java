package com.warehouse.adapter.dao.auditlog.products;

import org.springframework.data.repository.CrudRepository;

interface ProductsLogDatabase extends CrudRepository<ProductLogEntity, Long> {
}
