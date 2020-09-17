package com.warehouse.core;

import com.warehouse.adapter.security.AuthenticatedUser;

public interface Resource {

  boolean isOwnedBy(AuthenticatedUser user);
}
