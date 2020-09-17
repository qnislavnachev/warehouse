package com.warehouse.adapter.dao.role;

import com.warehouse.core.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
class PersistentRoleRepository implements RoleRepository {
  private final RoleDatabase database;

  public PersistentRoleRepository(RoleDatabase database) {
    this.database = database;
  }


  @Override
  public Role add(Role role) {
    RoleEntity roleEntity = database.save(new RoleEntity(role.getName()));

    return new Role(roleEntity.getId(), roleEntity.getName());
  }

  @Override
  public List<Role> getRoles(Set<Long> ids) {
    Iterable<RoleEntity> roleEntities = database.findAllById(ids);

    return StreamSupport.stream(roleEntities.spliterator(), false)
            .map(role -> new Role(role.getId(), role.getName()))
            .collect(Collectors.toList());
  }

  @Override
  public List<Role> getAll() {
    List<RoleEntity> roles = (List<RoleEntity>) database.findAll();

    return roles.stream()
            .map(role -> new Role(role.getId(), role.getName()))
            .collect(Collectors.toList());
  }
}
