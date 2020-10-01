package com.warehouse.adapter.dao.bi;

import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.core.Product;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class BiFacade {
  private final BIRepository biRepository;
  private final WarehouseRepository warehouseRepository;
  private final UserRepository userRepository;

  public BiFacade(BIRepository biRepository, WarehouseRepository warehouseRepository, UserRepository userRepository) {
    this.biRepository = biRepository;
    this.warehouseRepository = warehouseRepository;
    this.userRepository = userRepository;
  }

  public Double getUserOrdersProductsAverageAmount(Long userId) throws UserNotFoundException {
    User user = userRepository.getUser(userId);

    return biRepository.getUserOrdersProductsAverageAmount(user);
  }

  public Double getOrderedProductAverageAmount(Long productId) throws ProductNotFoundException {
    Product product = warehouseRepository.getProduct(productId);

    return biRepository.getOrderedProductAverageAmount(product);
  }

  public Double getOrderedProductAverageAmount(Long userId, Long productId) throws UserNotFoundException, ProductNotFoundException {
    User user = userRepository.getUser(userId);
    Product product = warehouseRepository.getProduct(productId);

    return biRepository.getOrderedProductAverageAmount(user, product);
  }
}
