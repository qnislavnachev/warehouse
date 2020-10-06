package com.warehouse.adapter.services;

import com.warehouse.adapter.dao.user.UserFacade;
import com.warehouse.core.User;
import com.warehouse.core.events.UserRegisteredEvent;
import com.warehouse.core.exceptions.RoleNotFoundException;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.core.exceptions.WalletNotFoundException;
import com.warehouse.eventbus.EventBus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
  private final EventBus eventBus;
  private final UserFacade userFacade;

  public UserService(EventBus eventBus, UserFacade userFacade) {
    this.eventBus = eventBus;
    this.userFacade = userFacade;
  }

  public User registerUser(User user, Set<Long> rolesIds) throws UserAlreadyExistsException, RoleNotFoundException {
    User registeredUser = userFacade.registerUser(user, rolesIds);

    eventBus.publish(new UserRegisteredEvent(user.getId(), user.getName(), user.getEmail()));

    return registeredUser;
  }

  public User getUser(Long id) throws UserNotFoundException {
    return userFacade.getUser(id);
  }

  public void depositToWallet(Long ownerId, Double amount) throws WalletNotFoundException {
    userFacade.depositToWallet(ownerId, amount);
  }
}
