package com.warehouse.adapter.http.dto;

import java.util.Collections;
import java.util.List;

public class RegisterUserRequest {
  public String name;
  public String email;
  public String password;
  public List<Long> roles;

  public RegisterUserRequest() {
    this.name = "";
    this.email = "";
    this.password = "";
    this.roles = Collections.emptyList();
  }

  public RegisterUserRequest(String name, String email, String password, List<Long> roles) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }
}
