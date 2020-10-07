package com.warehouse.adapter.services;

import com.warehouse.adapter.dao.bi.BiFacade;
import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.exports.ExportType;
import com.warehouse.exports.Exporter;
import com.warehouse.exports.ExporterFactory;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class BIService {

  private final BiFacade biFacade;
  private final WarehouseStorageFacade warehouseStorageFacade;
  private final ExporterFactory exporterFactory;

  public BIService(BiFacade biFacade, WarehouseStorageFacade warehouseStorageFacade, ExporterFactory exporterFactory) {
    this.biFacade = biFacade;
    this.warehouseStorageFacade = warehouseStorageFacade;
    this.exporterFactory = exporterFactory;
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

  public File generateProductsReport(ExportType exportType) throws NotSupportedException, SystemException {
    Exporter exporter = exporterFactory.get(exportType);
    List<Product> products = warehouseStorageFacade.getProducts();

    try {
      return exporter.export(products);

    } catch (IOException e) {
      throw new SystemException();
    }
  }
}
