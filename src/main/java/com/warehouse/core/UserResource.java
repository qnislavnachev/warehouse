package com.warehouse.core;

import com.warehouse.adapter.security.AuthenticatedUser;

public class UserResource implements Resource {
  private final Long userId;

  public UserResource(Long userId) {
    this.userId = userId;
  }

  @Override
  public boolean isOwnedBy(AuthenticatedUser user) {
    return user.getId().equals(userId);
  }
}
