package com.warehouse.payment;

import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.WalletNotFoundException;

import javax.transaction.NotSupportedException;

public interface PaymentStrategy {

  Order pay(Order order) throws WalletNotFoundException, NoEnoughAmountException, NotSupportedException, SystemException;
}
