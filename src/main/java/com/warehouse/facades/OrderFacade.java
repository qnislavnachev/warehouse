package com.warehouse.facades;

import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.OrderNotFoundException;
import com.warehouse.core.exceptions.ProductsWereNotFoundException;

import java.util.List;

public interface OrderFacade {

  Order createOrder(Long ownerId, List<OrderItem> orderItems) throws NoEnoughQuantityException, ProductsWereNotFoundException;

  Order getOrder(Long id) throws OrderNotFoundException;

  Order markAsPaid(Order order);
}
