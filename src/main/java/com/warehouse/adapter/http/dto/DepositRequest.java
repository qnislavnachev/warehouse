package com.warehouse.adapter.http.dto;

public class DepositRequest {
  public Double amount;

  public DepositRequest() {
    this.amount = 0.0;
  }

  public DepositRequest(Double amount) {
    this.amount = amount;
  }
}
