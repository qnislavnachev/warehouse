package com.warehouse.adapter.security;

import com.warehouse.core.Role;
import org.springframework.security.core.GrantedAuthority;

public class Authority implements GrantedAuthority {
  private final String authority;
  private static final String rolePrefix = "ROLE_";

  public Authority(Role role) {
    this.authority = rolePrefix + role.getName();
  }

  public Authority(String authority) {
    this.authority = authority;
  }

  @Override
  public String getAuthority() {
    return authority;
  }

  public boolean is(String roleName) {
    return authority.equalsIgnoreCase(rolePrefix + roleName);
  }
}
