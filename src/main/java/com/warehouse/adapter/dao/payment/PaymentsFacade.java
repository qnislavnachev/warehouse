package com.warehouse.adapter.dao.payment;

import com.warehouse.adapter.dao.order.OrderFacade;
import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.WalletNotFoundException;
import com.warehouse.payment.PaymentMethod;
import com.warehouse.payment.PaymentStrategy;
import com.warehouse.payment.PaymentStrategyFactory;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;

@Component
public class PaymentsFacade {
  private final WarehouseStorageFacade warehouseFacade;
  private final OrderFacade orderFacade;
  private final PaymentStrategyFactory paymentStrategyFactory;

  public PaymentsFacade(WarehouseStorageFacade warehouseFacade, OrderFacade orderFacade, PaymentStrategyFactory paymentStrategyFactory) {
    this.warehouseFacade = warehouseFacade;
    this.orderFacade = orderFacade;
    this.paymentStrategyFactory = paymentStrategyFactory;
  }

  @Transactional
  public Order payOrder(Order order, PaymentMethod paymentMethod) throws WalletNotFoundException, SystemException, NoEnoughAmountException, NotSupportedException {
    PaymentStrategy strategy = paymentStrategyFactory.get(paymentMethod);
    return strategy.pay(order);
  }
}
