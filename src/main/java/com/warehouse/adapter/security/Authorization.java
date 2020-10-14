package com.warehouse.adapter.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public class Authorization extends UsernamePasswordAuthenticationToken {

  public Authorization(Long userId, String email, List<Authority> authorities) {
    super(new AuthenticatedUser(userId, email, "", authorities), null, authorities);
  }
}
