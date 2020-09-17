package com.warehouse.adapter.security.jwt;

import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.WebTokenGenerator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtGenerator implements WebTokenGenerator {
  private final String secretKey;
  private final Long ttlSeconds;
  private final SignatureAlgorithm algorithm;

  public JwtGenerator(String secretKey, Long ttlSeconds, SignatureAlgorithm algorithm) {
    this.secretKey = secretKey;
    this.ttlSeconds = ttlSeconds;
    this.algorithm = algorithm;
  }

  @Override
  public String generate(AuthenticatedUser user) {
    Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) user.getAuthorities();

    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", user.getId());
    claims.put("authorities", authorities.stream().map(SimpleGrantedAuthority::getAuthority).collect(Collectors.toList()));

    return generate(user.getUsername(), claims);
  }

  private String generate(String subject, Map<String, Object> claims) {
    Instant issueDate = Instant.now();

    return Jwts.builder()
            .setSubject(subject)
            .addClaims(claims)
            .setIssuedAt(Date.from(issueDate))
            .setExpiration(Date.from(issueDate.plusSeconds(ttlSeconds)))
            .signWith(algorithm, secretKey)
            .compact();
  }
}
