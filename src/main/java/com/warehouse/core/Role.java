package com.warehouse.core;

public class Role {
  public final static String ADMIN_ROLE = "admin";

  private final Long id;
  private final String name;

  public Role(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Role(String name) {
    this.id = null;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
