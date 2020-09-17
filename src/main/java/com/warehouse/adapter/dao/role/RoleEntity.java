package com.warehouse.adapter.dao.role;

import com.warehouse.adapter.dao.user.UserEntity;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String name;

  @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
  private List<UserEntity> users;

  public RoleEntity() {
    this.id = 0L;
    this.name = "";
    this.users = Collections.emptyList();
  }

  public RoleEntity(Long id, String name) {
    this.id = id;
    this.name = name;
    this.users = Collections.emptyList();
  }

  public RoleEntity(String name) {
    this.id = null;
    this.name = name;
    this.users = Collections.emptyList();
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
