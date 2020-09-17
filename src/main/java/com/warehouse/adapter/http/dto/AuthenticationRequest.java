package com.warehouse.adapter.http.dto;

public class AuthenticationRequest {
  public String email;
  public String password;

  public AuthenticationRequest() {
    this.email = "";
    this.password = "";
  }
}
