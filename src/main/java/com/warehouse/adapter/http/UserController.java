package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.*;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.services.UserService;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.RoleNotFoundException;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.core.exceptions.WalletNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/v1")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ResponseEntity<Object> registerUser(@RequestBody RegisterUserRequest request) {
    try {
      User newUser = new User(request.name, request.email, request.password);
      User user = userService.registerUser(newUser, new HashSet<>(request.roles));

      return Response.created(UserDto.adapt(user));

    } catch (UserAlreadyExistsException e) {
      return Response.conflict(Error.of("User with email %s already exists", e.userEmail));

    } catch (RoleNotFoundException e) {
      return Response.notFound(Error.of("Some of the roles that were provided dont exists"));
    }
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<Object> getUser(@PathVariable Long id) {
    try {
      AuthenticatedUser currentUser = AuthenticatedUser.getCurrentUser();

      if (!currentUser.getId().equals(id) && !currentUser.isAdmin()) {
        return Response.forbidden(Error.of("The user does not have access to the given resource"));
      }

      User user = userService.getUser(id);
      return Response.ok(UserDto.adapt(user));

    } catch (UserNotFoundException e) {
      return Response.notFound(Error.of("User with id %s was not found", e.userId));
    }
  }

  @PutMapping("/users/{id}/wallet/deposit")
  public ResponseEntity<Object> depositToWallet(@PathVariable Long id, @RequestBody DepositRequest request) {
    try {
      AuthenticatedUser currentUser = AuthenticatedUser.getCurrentUser();

      if (!currentUser.getId().equals(id) && !currentUser.isAdmin()) {
        return Response.forbidden(Error.of("The user does not have access to the given resource"));
      }

      userService.depositToWallet(id, request.amount);
      return Response.ok();

    } catch (WalletNotFoundException e) {
      return Response.notFound(Error.of("Wallet with owner id %s was not found", e.ownerId));
    }
  }
}
