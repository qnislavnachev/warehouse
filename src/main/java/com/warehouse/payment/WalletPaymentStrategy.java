package com.warehouse.payment;

import com.warehouse.adapter.facades.OrderFacade;
import com.warehouse.adapter.facades.UserFacade;
import com.warehouse.adapter.facades.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.WalletNotFoundException;

class WalletPaymentStrategy extends AppPaymentStrategy {
  private final UserFacade userFacade;

  public WalletPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade, UserFacade userFacade) {
    super(orderFacade, warehouseFacade);
    this.userFacade = userFacade;
  }

  @Override
  public void collectMoney(Order order) throws NoEnoughAmountException, SystemException {
    try {
      userFacade.walletWithdraw(order.getOwnerId(), order.getPrice());

    } catch (WalletNotFoundException e) {
      // if this exception occur that means the order was created with invalid user
      // and we have system problems (bug or bugs)
      throw new SystemException(e);
    }
  }
}
