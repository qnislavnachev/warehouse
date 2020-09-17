package com.warehouse.adapter.dao.warehouse;

import org.springframework.data.jpa.repository.JpaRepository;


interface ProductDatabase extends JpaRepository<ProductEntity, Long> {

  ProductEntity findByName(String name);
}
