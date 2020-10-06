package com.warehouse.core.events;

public class UserRegisteredEvent implements Event {
  public final Long userId;
  public final String name;
  public final String email;

  public UserRegisteredEvent(Long userId, String name, String email) {
    this.userId = userId;
    this.name = name;
    this.email = email;
  }
}
