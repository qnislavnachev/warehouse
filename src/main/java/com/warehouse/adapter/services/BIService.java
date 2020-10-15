package com.warehouse.adapter.services;

import com.warehouse.facades.BiFacade;
import com.warehouse.facades.WarehouseStorageFacade;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.exports.Exporter;
import com.warehouse.exports.ExporterFactory;
import com.warehouse.exports.Report;
import com.warehouse.exports.ReportType;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
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

  public Report generateProductsReport(ReportType reportType) throws NotSupportedException {
    Exporter exporter = exporterFactory.get(reportType);
    List<Product> products = warehouseStorageFacade.getProducts();

    return exporter.generateReport(products);
  }
}
