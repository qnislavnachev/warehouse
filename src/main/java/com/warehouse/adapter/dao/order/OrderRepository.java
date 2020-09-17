package com.warehouse.adapter.dao.order;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.OrderNotFoundException;

public interface OrderRepository {

  Order create(Order order);

  Order getOrder(Long id) throws OrderNotFoundException;

  Order markAsPaid(Order order);
}
