package com.warehouse.facades;

import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UnableToSellStockException;

import java.util.List;

public interface WarehouseStorageFacade {

  Product getProduct(Long id) throws ProductNotFoundException;

  List<Product> getProducts();

  void addProducts(List<Product> products);

  void sellOrderStock(Order order) throws NoEnoughQuantityException, UnableToSellStockException;
}
