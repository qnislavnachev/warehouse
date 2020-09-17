package com.warehouse.adapter.dao.auditlog.products;

import com.warehouse.adapter.dao.auditlog.LogAction;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;

@Entity
class ProductLogEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long productId;
  private LogAction action;
  private String description;
  private Instant createdOn;

  public ProductLogEntity() {
    this.id = null;
    this.productId = null;
    this.action = null;
    this.description = "";
    this.createdOn = null;
  }

  public ProductLogEntity(Long productId, LogAction action, String description, Instant createdOn) {
    this.id = null;
    this.productId = productId;
    this.action = action;
    this.description = description;
    this.createdOn = createdOn;
  }
}
