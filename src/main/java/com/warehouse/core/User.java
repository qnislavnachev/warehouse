package com.warehouse.core;

import java.util.ArrayList;
import java.util.List;

public class User {
  private final Long id;
  private final String name;
  private final String email;
  private final String password;
  private final Wallet wallet;
  private final List<Role> roles;

  public User(Long id, String name, String email, String password, List<Role> roles, Wallet wallet) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.wallet = wallet;
    this.roles = roles;
  }

  public User(String name, String email, String password) {
    this.id = null;
    this.name = name;
    this.email = email;
    this.password = password;
    this.wallet = new Wallet(0.0);
    this.roles = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public Wallet getWallet() {
    return wallet;
  }

  public List<Role> getRoles() {
    return roles;
  }
}
