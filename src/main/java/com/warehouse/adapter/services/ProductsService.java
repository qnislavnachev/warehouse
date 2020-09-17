package com.warehouse.adapter.services;

import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Product;
import com.warehouse.core.events.ProductsLoadedEvent;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.eventbus.EventBus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsService {
  private final EventBus eventBus;
  private final WarehouseStorageFacade warehouseFacade;

  public ProductsService(EventBus eventBus, WarehouseStorageFacade warehouseStorage) {
    this.eventBus = eventBus;
    this.warehouseFacade = warehouseStorage;
  }

  public void loadProducts(List<Product> products) {
    warehouseFacade.addProducts(products);
    eventBus.publish(new ProductsLoadedEvent(products));
  }

  public Product getProduct(Long id) throws ProductNotFoundException {
    return warehouseFacade.getProduct(id);
  }

  public List<Product> getAllProducts() {
    return warehouseFacade.getProducts();
  }
}
