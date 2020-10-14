package com.warehouse.adapter.security.jwt;

import com.warehouse.adapter.security.Authority;
import com.warehouse.adapter.security.WebToken;
import io.jsonwebtoken.Claims;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class Jwt implements WebToken {
  private final Claims claims;

  public Jwt(Claims claims) {
    this.claims = claims;
  }

  @Override
  public Long getUserId() {
    return claims.get("userId", Long.class);
  }

  @Override
  public String getEmail() {
    return claims.getSubject();
  }

  @Override
  public boolean isExpired() {
    return Date.from(Instant.now()).after(claims.getExpiration());
  }

  @Override
  public List<Authority> getAuthorities() {
    List<String> authoritiesClaims = claims.get("authorities", List.class);

    if (isNull(authoritiesClaims) || authoritiesClaims.isEmpty()) {
      return new ArrayList<>();
    }

    return authoritiesClaims.stream()
            .map(Authority::new)
            .collect(Collectors.toList());
  }
}
