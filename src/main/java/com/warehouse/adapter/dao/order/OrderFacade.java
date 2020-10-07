package com.warehouse.adapter.dao.order;

import com.warehouse.adapter.dao.warehouse.ReservationRequest;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.Reservation;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.OrderNotFoundException;
import com.warehouse.core.exceptions.ProductsWereNotFoundException;
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
    ReservationRequest reservationRequest = ReservationRequest.of(orderItems);
    Reservation reservation = warehouseRepository.reserveProducts(reservationRequest);

    Order order = new Order(ownerId, orderItems, reservation.getTotalPrice(), false);

    return orderRepository.create(order);
  }

  public Order getOrder(Long id) throws OrderNotFoundException {
    return orderRepository.getOrder(id);
  }

  public Order markAsPaid(Order order) {
    return orderRepository.markAsPaid(order);
  }
}
