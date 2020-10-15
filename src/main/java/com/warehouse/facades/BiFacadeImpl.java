package com.warehouse.facades;

import com.warehouse.adapter.dao.bi.BIRepository;
import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.core.Product;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BiFacadeImpl implements BiFacade {
  private final BIRepository biRepository;
  private final WarehouseRepository warehouseRepository;
  private final UserRepository userRepository;

  public BiFacadeImpl(BIRepository biRepository, WarehouseRepository warehouseRepository, UserRepository userRepository) {
    this.biRepository = biRepository;
    this.warehouseRepository = warehouseRepository;
    this.userRepository = userRepository;
  }

  @Override
  public Double getUserOrdersProductsAverageAmount(Long userId) throws UserNotFoundException {
    User user = userRepository.getUser(userId);

    return biRepository.getUserOrdersProductsAverageAmount(user);
  }

  @Override
  public Double getOrderedProductAverageAmount(Long productId) throws ProductNotFoundException {
    Product product = warehouseRepository.getProduct(productId);

    return biRepository.getOrderedProductAverageAmount(product);
  }

  @Override
  public Double getOrderedProductAverageAmount(Long userId, Long productId) throws UserNotFoundException, ProductNotFoundException {
    User user = userRepository.getUser(userId);
    Product product = warehouseRepository.getProduct(productId);

    return biRepository.getOrderedProductAverageAmount(user, product);
  }
}
