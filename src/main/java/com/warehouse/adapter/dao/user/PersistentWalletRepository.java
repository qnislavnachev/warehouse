package com.warehouse.adapter.dao.user;

import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.WalletNotFoundException;
import org.springframework.stereotype.Repository;

import static java.util.Objects.isNull;

@Repository
class PersistentWalletRepository implements WalletRepository {
  private final WalletDatabase database;

  public PersistentWalletRepository(WalletDatabase database) {
    this.database = database;
  }

  @Override
  public void deposit(Long ownerId, Double amount) throws WalletNotFoundException {
    WalletEntity walletEntity = database.findByOwnerId(ownerId);

    if (isNull(walletEntity)) {
      throw new WalletNotFoundException(ownerId);
    }

    walletEntity.deposit(amount);

    database.save(walletEntity);
  }

  @Override
  public void withdraw(Long ownerId, Double amount) throws NoEnoughAmountException, WalletNotFoundException {
    WalletEntity walletEntity = database.findByOwnerId(ownerId);

    if (isNull(walletEntity)) {
      throw new WalletNotFoundException(ownerId);
    }

    boolean isSuccessful = walletEntity.withdraw(amount);

    if (!isSuccessful) {
      throw new NoEnoughAmountException();
    }

    database.save(walletEntity);
  }
}
