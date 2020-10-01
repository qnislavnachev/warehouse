package com.warehouse.adapter.services;

import com.warehouse.adapter.dao.bi.BiFacade;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BIService {

  private final BiFacade biFacade;

  public BIService(BiFacade biFacade) {
    this.biFacade = biFacade;
  }

  public Double getUserOrdersProductsAverageAmount(Long userId) throws UserNotFoundException {
    return biFacade.getUserOrdersProductsAverageAmount(userId);
  }

  public Double getOrderedProductAverageAmount(Long productId) throws ProductNotFoundException {
    return biFacade.getOrderedProductAverageAmount(productId);
  }

  public Double getOrderedProductAverageAmount(Long userId, Long productId) throws UserNotFoundException, ProductNotFoundException {
    return biFacade.getOrderedProductAverageAmount(userId, productId);
  }
}
