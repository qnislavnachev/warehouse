package com.warehouse.adapter.dao.user;

import org.springframework.data.repository.CrudRepository;

interface WalletDatabase extends CrudRepository<WalletEntity, Long> {

  WalletEntity findByOwnerId(Long ownerId);
}
