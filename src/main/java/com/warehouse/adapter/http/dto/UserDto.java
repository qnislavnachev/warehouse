package com.warehouse.adapter.http.dto;

import com.warehouse.core.Role;
import com.warehouse.core.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
  public Long id;
  public String name;
  public String email;
  public Double walletBalance;
  public List<String> roles;

  public UserDto() {
    this.id = null;
    this.name = "";
    this.email = "";
    this.walletBalance = 0.0;
    this.roles = Collections.emptyList();
  }

  public UserDto(Long id, String name, String email, Double walletBalance, List<String> roles) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.walletBalance = walletBalance;
    this.roles = roles;
  }

  public static UserDto adapt(User user) {
    List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());

    return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getWallet().getBalance(), roles);
  }
}
