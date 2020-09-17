package com.warehouse.payment;

import com.warehouse.adapter.dao.order.OrderFacade;
import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.WalletNotFoundException;

class CreditCardPaymentStrategy extends AppPaymentStrategy {

  public CreditCardPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade) {
    super(orderFacade, warehouseFacade);
  }

  @Override
  public void collectMoney(Order order) throws WalletNotFoundException, NoEnoughAmountException {
    // imagine that we call external api for credit card payment
  }
}
