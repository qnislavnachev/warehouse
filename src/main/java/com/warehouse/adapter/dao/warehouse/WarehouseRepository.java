package com.warehouse.adapter.dao.warehouse;

import com.warehouse.core.Order;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.ProductsWereNotFoundException;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.SellProcessException;

import java.util.List;

public interface WarehouseRepository {

  Product getProduct(Long id) throws ProductNotFoundException;

  List<Product> getProducts();

  List<Product> addProducts(List<Product> products);

  List<Product>  reserveProducts(ReservationRequest reservationRequest) throws NoEnoughQuantityException, ProductsWereNotFoundException;

  void sellOrder(Order order) throws NoEnoughQuantityException, SellProcessException;
}
