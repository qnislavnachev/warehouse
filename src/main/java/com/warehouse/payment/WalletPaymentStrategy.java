package com.warehouse.payment;

import com.warehouse.adapter.dao.order.OrderFacade;
import com.warehouse.adapter.dao.user.UserFacade;
import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.WalletNotFoundException;

class WalletPaymentStrategy extends AppPaymentStrategy {
  private final UserFacade userFacade;

  public WalletPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade, UserFacade userFacade) {
    super(orderFacade, warehouseFacade);
    this.userFacade = userFacade;
  }

  @Override
  public void collectMoney(Order order) throws WalletNotFoundException, NoEnoughAmountException {
    userFacade.walletWithdraw(order.getOwnerId(), order.getPrice());
  }
}
