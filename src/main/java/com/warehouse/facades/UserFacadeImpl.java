package com.warehouse.facades;

import com.warehouse.adapter.dao.role.RoleRepository;
import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.dao.user.WalletRepository;
import com.warehouse.core.Role;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class UserFacadeImpl implements UserFacade {
  private final UserRepository userRepository;
  private final WalletRepository walletRepository;
  private final RoleRepository roleRepository;

  public UserFacadeImpl(UserRepository userRepository, WalletRepository walletRepository, RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.walletRepository = walletRepository;
    this.roleRepository = roleRepository;
  }

  public User registerUser(User user, Set<Long> rolesIds) throws UserAlreadyExistsException, RoleNotFoundException {
    if (rolesIds.isEmpty()) {
      return userRepository.register(user, new ArrayList<>());
    }

    List<Role> roles = roleRepository.getRoles(rolesIds);
    if (roles.size() != rolesIds.size()) {
      //TODO: provide an info for the missing roles
      throw new RoleNotFoundException();
    }

    return userRepository.register(user, roles);
  }

  public User getUser(Long id) throws UserNotFoundException {
    return userRepository.getUser(id);
  }

  public User getUser(String email) throws UserNotFoundException {
    return userRepository.getUser(email);
  }

  public void depositToWallet(Long userId, Double amount) throws WalletNotFoundException {
    walletRepository.deposit(userId, amount);
  }

  public void walletWithdraw(Long userId, Double amount) throws NoEnoughAmountException, WalletNotFoundException {
    walletRepository.withdraw(userId, amount);
  }
}
