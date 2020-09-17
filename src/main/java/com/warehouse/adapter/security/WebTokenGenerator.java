package com.warehouse.adapter.security;

public interface WebTokenGenerator {

  String generate(AuthenticatedUser user);
}
