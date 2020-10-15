package com.warehouse.facades;

import com.warehouse.adapter.dao.role.RoleRepository;
import com.warehouse.core.Role;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleFacadeImpl implements RoleFacade {
  private final RoleRepository repository;

  public RoleFacadeImpl(RoleRepository repository) {
    this.repository = repository;
  }

  public void add(Role role) {
    repository.add(role);
  }

  public List<Role> getAll() {
    return repository.getAll();
  }
}
