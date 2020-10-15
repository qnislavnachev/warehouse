package com.warehouse.adapter.dao.user;

import javax.persistence.*;

@Entity
class WalletEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  private UserEntity owner;
  private Double balance;

  public WalletEntity() {
    this.id = null;
    this.owner = null;
    this.balance = null;
  }

  private WalletEntity(UserEntity owner, Double balance) {
    this.id = null;
    this.owner = owner;
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public UserEntity getOwner() {
    return owner;
  }

  public Double getBalance() {
    return balance;
  }

  public void deposit(Double amount) {
    balance += amount;
  }

  public boolean withdraw(Double amount) {
    if (balance < amount) {
      return false;
    }

    balance -= amount;
    return true;
  }

  public static WalletEntity newWallet(UserEntity owner, Double balance) {
    return new WalletEntity(owner, balance);
  }
}
