package com.warehouse.adapter.http.filters;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.security.Authorization;
import com.warehouse.adapter.security.WebToken;
import com.warehouse.adapter.security.WebTokenParser;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static java.util.Objects.nonNull;

public class SecurityFilter extends HttpRequestFilter {
  private final static String AUTH_PREFIX = "Bearer ";
  private final Set<String> excludedEndpoints;

  private final WebTokenParser tokenParser;

  public SecurityFilter(Set<String> excludedEndpoints, WebTokenParser tokenParser) {
    this.excludedEndpoints = excludedEndpoints;
    this.tokenParser = tokenParser;
  }

  @Override
  public ResponseEntity<Object> filter(HttpServletRequest request) {
    String authentication = request.getHeader("Authorization");

    if (excludedEndpoints.contains(request.getRequestURI())) {
      return Response.ok();
    }

    if (!isBearer(authentication)) {
      return Response.forbidden(Error.of("User is not authorized"));
    }

    try {
      String jwtValue = authentication.substring(AUTH_PREFIX.length());
      WebToken token = tokenParser.parse(jwtValue);

      if (token.isExpired()) {
        return Response.forbidden("User token has expired");
      }

      Authorization authorization = new Authorization(token.getUserId(), token.getEmail(), token.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authorization);

      return Response.ok();
    } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
      return Response.forbidden(Error.of("User is not authorized"));
    }
  }

  private boolean isBearer(String value) {
    return nonNull(value) && !"".equals(value) && value.startsWith(AUTH_PREFIX);
  }
}
