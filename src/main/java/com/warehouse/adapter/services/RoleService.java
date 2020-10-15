package com.warehouse.adapter.services;

import com.warehouse.facades.RoleFacade;
import com.warehouse.core.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
  private final RoleFacade roleFacade;

  public RoleService(RoleFacade roleFacade) {
    this.roleFacade = roleFacade;
  }

  public void addRole(Role role) {
    roleFacade.add(role);
  }

  public List<Role> getAllRoles() {
    return roleFacade.getAll();
  }
}
