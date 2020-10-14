package com.warehouse.adapter.security;

import java.util.List;

public interface WebToken {

  Long getUserId();

  String getEmail();

  boolean isExpired();

  List<Authority> getAuthorities();
}
