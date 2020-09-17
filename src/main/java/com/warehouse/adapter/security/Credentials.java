package com.warehouse.adapter.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class Credentials extends UsernamePasswordAuthenticationToken {

  public Credentials(String email, String password) {
    super(email, password);
  }
}
