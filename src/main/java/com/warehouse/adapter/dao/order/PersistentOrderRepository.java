package com.warehouse.adapter.dao.order;

import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.exceptions.OrderNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
class PersistentOrderRepository implements OrderRepository {
  private final OrderDatabase database;

  public PersistentOrderRepository(OrderDatabase database) {
    this.database = database;
  }

  @Override
  public Order create(Order order) {
    OrderEntity orderEntity = new OrderEntity(order.getOwnerId(), order.getPrice());

    List<OrderItemEntity> orderItems = adapt(order.getOrderItems(), orderEntity);
    orderEntity.setOrderItems(orderItems);

    database.save(orderEntity);

    return adapt(orderEntity);
  }

  @Override
  public Order getOrder(Long id) throws OrderNotFoundException {
    Optional<OrderEntity> possibleEntity = database.findById(id);

    if (!possibleEntity.isPresent()) {
      throw new OrderNotFoundException(id);
    }

    OrderEntity orderEntity = possibleEntity.get();

    return adapt(orderEntity);
  }

  @Override
  public Order markAsPaid(Order order) {
    database.pay(order.getId());
    database.flush();
//    OrderEntity orderEntity = database.getOne(order.getId());
//
//    orderEntity.markAsPaid();
//
//    database.save(orderEntity);

    return new Order(order.getId(), order.getOwnerId(), order.getOrderItems(), order.getPrice(), true);
  }

  private Order adapt(OrderEntity entity) {
    return new Order(entity.getId(), entity.getOwnerId(), adapt(entity.getOrderItems()), entity.getPrice(), entity.isPaid());
  }

  private List<OrderItem> adapt(List<OrderItemEntity> productOrderEntities) {
    List<OrderItem> orderItems = new ArrayList<>();

    for (OrderItemEntity entity : productOrderEntities) {
      orderItems.add(new OrderItem(entity.getProductId(), entity.getQuantity()));
    }

    return orderItems;
  }

  private List<OrderItemEntity> adapt(List<OrderItem> orderItems, OrderEntity order) {
    List<OrderItemEntity> productOrdersEntities = new ArrayList<>();

    for (OrderItem orderItem : orderItems) {
      productOrdersEntities.add(OrderItemEntity.from(orderItem, order));
    }

    return productOrdersEntities;
  }
}
