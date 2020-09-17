package com.warehouse.core.exceptions;

public class UserAlreadyExistsException extends Exception {
  public final String userEmail;

  public UserAlreadyExistsException(String userEmail) {
    this.userEmail = userEmail;
  }
}
