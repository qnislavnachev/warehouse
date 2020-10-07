package com.warehouse.payment;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.InvalidPaymentSourceException;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;

import javax.transaction.NotSupportedException;

public interface PaymentStrategy {

  Order pay(Order order) throws InvalidPaymentSourceException, NoEnoughAmountException, NotSupportedException, SystemException;
}
