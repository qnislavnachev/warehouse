package com.warehouse.adapter.dao.user;

import org.springframework.data.repository.CrudRepository;

interface UserDatabase extends CrudRepository<UserEntity, Long> {

  UserEntity findByEmail(String name);
}
