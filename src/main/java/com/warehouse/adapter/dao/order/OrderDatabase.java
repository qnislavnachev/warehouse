package com.warehouse.adapter.dao.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

interface OrderDatabase extends JpaRepository<OrderEntity, Long> {

  @Transactional
  @Modifying(clearAutomatically = true)
  @Query("UPDATE OrderEntity o SET o.isPaid = true WHERE o.id = :orderId")
  void pay(@Param("orderId") Long orderId);
}
