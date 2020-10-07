package com.warehouse.adapter.dao.payment;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.InvalidPaymentSourceException;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.payment.PaymentMethod;
import com.warehouse.payment.PaymentStrategy;
import com.warehouse.payment.PaymentStrategyFactory;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;

@Component
public class PaymentsFacade {
  private final PaymentStrategyFactory paymentStrategyFactory;

  public PaymentsFacade(PaymentStrategyFactory paymentStrategyFactory) {
    this.paymentStrategyFactory = paymentStrategyFactory;
  }

  @Transactional
  public Order payOrder(Order order, PaymentMethod paymentMethod) throws SystemException, NoEnoughAmountException, NotSupportedException, InvalidPaymentSourceException {
    PaymentStrategy strategy = paymentStrategyFactory.get(paymentMethod);
    return strategy.pay(order);
  }
}
