package com.warehouse.facades;

import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;

public interface BiFacade {

  Double getUserOrdersProductsAverageAmount(Long userId) throws UserNotFoundException;

  Double getOrderedProductAverageAmount(Long productId) throws ProductNotFoundException;

  Double getOrderedProductAverageAmount(Long userId, Long productId) throws UserNotFoundException, ProductNotFoundException;
}
