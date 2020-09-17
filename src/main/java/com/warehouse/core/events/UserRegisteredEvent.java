package com.warehouse.core.events;

import com.warehouse.core.User;

public class UserRegisteredEvent implements Event {
  public final User user;

  public UserRegisteredEvent(User user) {
    this.user = user;
  }
}
