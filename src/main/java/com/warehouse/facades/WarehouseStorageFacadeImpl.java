package com.warehouse.facades;

import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UnableToSellStockException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WarehouseStorageFacadeImpl implements WarehouseStorageFacade {
  private final WarehouseRepository warehouseRepository;

  public WarehouseStorageFacadeImpl(WarehouseRepository warehouseRepository) {
    this.warehouseRepository = warehouseRepository;
  }

  public Product getProduct(Long id) throws ProductNotFoundException {
    return warehouseRepository.getProduct(id);
  }

  public List<Product> getProducts() {
    return warehouseRepository.getProducts();
  }

  public void addProducts(List<Product> products) {
    warehouseRepository.addProducts(products);
  }

  public void sellOrderStock(Order order) throws NoEnoughQuantityException, UnableToSellStockException {
    warehouseRepository.sellOrderStock(order);
  }
}
