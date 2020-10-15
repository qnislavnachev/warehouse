package com.warehouse.facades;

import com.warehouse.core.Role;

import java.util.List;

public interface RoleFacade {

  void add(Role role);

  List<Role> getAll();
}
