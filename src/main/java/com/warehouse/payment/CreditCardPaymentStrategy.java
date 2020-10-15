package com.warehouse.payment;

import com.warehouse.facades.OrderFacade;
import com.warehouse.facades.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;

class CreditCardPaymentStrategy extends AppPaymentStrategy {

  public CreditCardPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade) {
    super(orderFacade, warehouseFacade);
  }

  @Override
  public void collectMoney(Order order) throws NoEnoughAmountException {
    // imagine that we call external api for credit card payment
  }
}
