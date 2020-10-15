package com.warehouse.facades;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.payment.PaymentMethod;
import com.warehouse.payment.PaymentStrategy;
import com.warehouse.payment.PaymentStrategyFactory;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;

@Component
public class PaymentsFacadeImpl implements PaymentsFacade {
  private final PaymentStrategyFactory paymentStrategyFactory;

  public PaymentsFacadeImpl(PaymentStrategyFactory paymentStrategyFactory) {
    this.paymentStrategyFactory = paymentStrategyFactory;
  }

  @Transactional
  @Override
  public Order payOrder(Order order, PaymentMethod paymentMethod) throws SystemException, NoEnoughAmountException, NotSupportedException {
    PaymentStrategy strategy = paymentStrategyFactory.get(paymentMethod);
    return strategy.pay(order);
  }
}
