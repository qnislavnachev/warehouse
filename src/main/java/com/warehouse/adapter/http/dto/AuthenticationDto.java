package com.warehouse.adapter.http.dto;

public class AuthenticationDto {
  public String token;

  public AuthenticationDto() {
    this.token = "";
  }

  public AuthenticationDto(String token) {
    this.token = token;
  }
}
