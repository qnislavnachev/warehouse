package com.warehouse.adapter.services;

import com.warehouse.adapter.facades.OrderFacade;
import com.warehouse.adapter.facades.PaymentsFacade;
import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.events.OrderCreatedEvent;
import com.warehouse.core.events.OrderPaidEvent;
import com.warehouse.core.exceptions.*;
import com.warehouse.eventbus.EventBus;
import com.warehouse.payment.PaymentMethod;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.util.List;

@Service
public class OrderService {
  private final EventBus eventBus;
  private final OrderFacade orderFacade;
  private final PaymentsFacade paymentsFacade;

  public OrderService(EventBus eventBus, OrderFacade orderFacade, PaymentsFacade paymentsFacade) {
    this.eventBus = eventBus;
    this.orderFacade = orderFacade;
    this.paymentsFacade = paymentsFacade;
  }

  public Order createOrder(Long ownerId, List<OrderItem> orderItems) throws NoEnoughQuantityException, ProductsWereNotFoundException {
    Order order = orderFacade.createOrder(ownerId, orderItems);
    eventBus.publish(new OrderCreatedEvent(order.duplicate()));

    return order;
  }

  public Order getOrder(Long id) throws OrderNotFoundException {
    return orderFacade.getOrder(id);
  }

  public Order payOrder(Order order, PaymentMethod paymentMethod) throws SystemException, NoEnoughAmountException, NotSupportedException {
    Order paidOrder = paymentsFacade.payOrder(order, paymentMethod);

    eventBus.publish(new OrderPaidEvent(paidOrder.duplicate()));

    return paidOrder;
  }
}
