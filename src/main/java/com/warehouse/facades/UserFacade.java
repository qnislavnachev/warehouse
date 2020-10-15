package com.warehouse.facades;

import com.warehouse.core.User;
import com.warehouse.core.exceptions.*;

import java.util.Set;

public interface UserFacade {

  User registerUser(User user, Set<Long> rolesIds) throws UserAlreadyExistsException, RoleNotFoundException;

  User getUser(Long id) throws UserNotFoundException;

  User getUser(String email) throws UserNotFoundException;

  void depositToWallet(Long userId, Double amount) throws WalletNotFoundException;

  void walletWithdraw(Long userId, Double amount) throws NoEnoughAmountException, WalletNotFoundException;
}
