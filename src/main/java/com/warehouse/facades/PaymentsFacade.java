package com.warehouse.facades;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.payment.PaymentMethod;

import javax.transaction.NotSupportedException;

public interface PaymentsFacade {

  Order payOrder(Order order, PaymentMethod paymentMethod) throws SystemException, NoEnoughAmountException, NotSupportedException;
}
