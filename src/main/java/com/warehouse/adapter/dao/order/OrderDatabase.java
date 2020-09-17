package com.warehouse.adapter.dao.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface OrderDatabase extends JpaRepository<OrderEntity, Long> {

  @Query("UPDATE OrderEntity SET isPaid = true WHERE id = :orderId")
  void pay(@Param("orderId") Long orderId);
}
