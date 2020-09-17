package com.warehouse.adapter.dao.user;

import com.warehouse.core.Role;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import com.warehouse.core.exceptions.UserNotFoundException;

import java.util.List;

public interface UserRepository {

  User register(User user, List<Role> roles) throws UserAlreadyExistsException;

  User getUser(Long id) throws UserNotFoundException;

  User getUser(String email) throws UserNotFoundException;
}
