package com.warehouse.adapter.dao.user;

import com.warehouse.adapter.dao.role.RoleEntity;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;

  @Column(unique = true)
  private String email;
  private String password;

  @ManyToMany
  private List<RoleEntity> roles;

  @OneToOne(cascade = CascadeType.ALL)
  private WalletEntity wallet;

  public UserEntity() {
    this.id = null;
    this.name = "";
    this.email = "";
    this.wallet = null;
    this.password = "";
    this.roles = Collections.emptyList();
  }

  public UserEntity(String name, String email, String password, List<RoleEntity> roles) {
    this.id = null;
    this.name = name;
    this.email = email;
    this.wallet = null;
    this.password = password;
    this.roles = roles;
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

  public WalletEntity getWallet() {
    return wallet;
  }

  public void setWallet(WalletEntity wallet) {
    this.wallet = wallet;
  }

  public List<RoleEntity> getRoles() {
    return roles;
  }
}
