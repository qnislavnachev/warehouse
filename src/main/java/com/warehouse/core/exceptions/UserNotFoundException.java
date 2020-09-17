package com.warehouse.core.exceptions;

public class UserNotFoundException extends Exception {
  public final Long userId;
  public final String userEmail;

  public UserNotFoundException(Long userId) {
    this.userId = userId;
    this.userEmail = null;
  }

  public UserNotFoundException(String email) {
    this.userId = null;
    this.userEmail = email;
  }
}
