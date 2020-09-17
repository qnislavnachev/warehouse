package com.warehouse.adapter.http.dto;

import com.warehouse.core.Role;

import java.util.List;
import java.util.stream.Collectors;

public class RoleDto {
  public Long id;
  public String name;

  public RoleDto() {
    this.id = null;
    this.name = "";
  }

  private RoleDto(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public static List<RoleDto> adapt(List<Role> roles) {
    return roles.stream()
            .map(role -> new RoleDto(role.getId(), role.getName()))
            .collect(Collectors.toList());
  }
}
