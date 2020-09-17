package com.warehouse.adapter.dao.order;

import com.warehouse.adapter.dao.warehouse.ReservationRequest;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.Product;
import com.warehouse.core.exceptions.ProductsWereNotFoundException;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
public class OrderFacade {
  private final OrderRepository orderRepository;
  private final WarehouseRepository warehouseRepository;

  public OrderFacade(OrderRepository orderRepository, WarehouseRepository warehouseRepository) {
    this.orderRepository = orderRepository;
    this.warehouseRepository = warehouseRepository;
  }

  @Transactional
  public Order create(Long ownerId, List<OrderItem> orderItems) throws NoEnoughQuantityException, ProductsWereNotFoundException {
    ReservationRequest reservationRequest = ReservationRequest.from(orderItems);
    List<Product> products = warehouseRepository.reserveProducts(reservationRequest);

    Double orderPrice = calculatePrice(products);
    Order order = new Order(ownerId, orderItems, orderPrice, false);

    return orderRepository.create(order);
  }

  public Order getOrder(Long id) throws OrderNotFoundException {
    return orderRepository.getOrder(id);
  }

  public Order markAsPaid(Order order) {
    return orderRepository.markAsPaid(order);
  }

  private Double calculatePrice(List<Product> products) {
    Double price = 0.0;

    for (Product product : products) {
      price += product.getTotalPrice();
    }

    return price;
  }
}
