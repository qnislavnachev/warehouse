package com.warehouse.adapter.dao.role;

import com.warehouse.core.Role;

import java.util.List;
import java.util.Set;

public interface RoleRepository {

  Role add(Role role);

  List<Role> getRoles(Set<Long> ids);

  List<Role> getAll();
}
