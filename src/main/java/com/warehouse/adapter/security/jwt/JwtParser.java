package com.warehouse.adapter.security.jwt;

import com.warehouse.adapter.security.WebTokenParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class JwtParser implements WebTokenParser {
  private final io.jsonwebtoken.JwtParser parser;

  public JwtParser(String secretKey) {
    this.parser = Jwts.parser().setSigningKey(secretKey);
  }

  public Jwt parse(String token) {
    Jws<Claims> claims = parser.parseClaimsJws(token);


    return new Jwt(claims.getBody());
  }
}
