package com.warehouse.adapter.dao.auditlog.orders;

import com.warehouse.adapter.dao.auditlog.LogAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
class OrderLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long orderId;
  private LogAction action;
  private String description;
  private Instant createdOn;

  public OrderLogEntity() {
    this.id = null;
    this.orderId = null;
    this.action = null;
    this.description = "";
    this.createdOn = null;
  }

  public OrderLogEntity(Long orderId, LogAction action, String description, Instant createdOn) {
    this.id = null;
    this.orderId = orderId;
    this.action = action;
    this.description = description;
    this.createdOn = createdOn;
  }
}
