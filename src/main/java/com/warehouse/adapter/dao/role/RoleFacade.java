package com.warehouse.adapter.dao.role;

import com.warehouse.core.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleFacade {
  private final RoleRepository repository;

  public RoleFacade(RoleRepository repository) {
    this.repository = repository;
  }

  public void add(Role role) {
    repository.add(role);
  }

  public List<Role> getAll() {
    return repository.getAll();
  }
}
