package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.AuthenticationDto;
import com.warehouse.adapter.http.dto.AuthenticationRequest;
import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.Credentials;
import com.warehouse.adapter.security.WebTokenGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {
  private final AuthenticationManager authManager;
  private final WebTokenGenerator tokenGenerator;

  public SecurityController(AuthenticationManager authManager, WebTokenGenerator tokenGenerator) {
    this.authManager = authManager;
    this.tokenGenerator = tokenGenerator;
  }

  @PostMapping("/auth")
  public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request) {
    Credentials credentials = new Credentials(request.email, request.password);

    try {
      Authentication authorization = authManager.authenticate(credentials);
      AuthenticatedUser user = (AuthenticatedUser) authorization.getPrincipal();

      String token = tokenGenerator.generate(user);

      return Response.ok(new AuthenticationDto(token));

    } catch (AuthenticationException e) {
      return Response.forbidden(Error.of("Username or password is invalid"));
    }
  }
}
