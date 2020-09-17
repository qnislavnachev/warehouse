package com.warehouse.adapter.dao.user;

import com.warehouse.adapter.dao.role.RoleEntity;
import com.warehouse.core.Role;
import com.warehouse.core.User;
import com.warehouse.core.Wallet;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import com.warehouse.core.exceptions.UserNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Repository
class PersistentUserRepository implements UserRepository {
  private final UserDatabase database;

  public PersistentUserRepository(UserDatabase database) {
    this.database = database;
  }

  @Override
  public User register(User user, List<Role> roles) throws UserAlreadyExistsException {
    UserEntity userEntity = database.findByEmail(user.getEmail());

    if (nonNull(userEntity)) {
      throw new UserAlreadyExistsException(user.getEmail());
    }

    UserEntity newUserEntity = new UserEntity(user.getName(), user.getEmail(), user.getPassword(), toEntity(roles));
    WalletEntity walletEntity = WalletEntity.newWallet(newUserEntity, user.getWallet().getBalance());
    newUserEntity.setWallet(walletEntity);

    UserEntity savedUser = database.save(newUserEntity);

    return adapt(savedUser);
  }

  @Override
  public User getUser(Long id) throws UserNotFoundException {
    Optional<UserEntity> possibleEntity = database.findById(id);

    if (!possibleEntity.isPresent()) {
      throw new UserNotFoundException(id);
    }

    return adapt(possibleEntity.get());
  }

  @Override
  public User getUser(String email) throws UserNotFoundException {
    UserEntity userEntity = database.findByEmail(email);

    if (isNull(userEntity)) {
      throw new UserNotFoundException(email);
    }

    return adapt(userEntity);
  }

  private User adapt(UserEntity entity) {
    WalletEntity walletEntity = entity.getWallet();

    return new User(entity.getId(), entity.getName(), entity.getEmail(), entity.getPassword(), adapt(entity.getRoles()),
            new Wallet(walletEntity.getId(), walletEntity.getBalance()));
  }

  private List<RoleEntity> toEntity(List<Role> roles) {
    return roles.stream()
            .map(it -> new RoleEntity(it.getId(), it.getName()))
            .collect(Collectors.toList());
  }

  private List<Role> adapt(List<RoleEntity> roles) {
    return roles.stream()
            .map(it -> new Role(it.getId(), it.getName()))
            .collect(Collectors.toList());
  }
}
