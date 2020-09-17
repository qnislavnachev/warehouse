package com.warehouse.adapter.dao.user;

import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.WalletNotFoundException;

public interface WalletRepository {

  void deposit(Long ownerId, Double amount) throws WalletNotFoundException;

  void withdraw(Long ownerId, Double amount) throws WalletNotFoundException, NoEnoughAmountException;
}
