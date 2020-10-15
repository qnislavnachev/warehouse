package com.warehouse.payment;

import com.warehouse.facades.OrderFacade;
import com.warehouse.facades.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.NoEnoughAmountException;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.UnableToSellStockException;

abstract class AppPaymentStrategy implements PaymentStrategy {
  private final OrderFacade orderFacade;
  private final WarehouseStorageFacade warehouseFacade;

  protected AppPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade) {
    this.orderFacade = orderFacade;
    this.warehouseFacade = warehouseFacade;
  }

  @Override
  public Order pay(Order order) throws NoEnoughAmountException, SystemException {
    Order paidOrder = orderFacade.markAsPaid(order);

    try {
      warehouseFacade.sellOrderStock(paidOrder);

    } catch (NoEnoughQuantityException | UnableToSellStockException e) {
      // if some of these exception occurred it is because of some system problem
      // if the business logic is fine these exceptions should not appear
      throw new SystemException(e);
    }

    collectMoney(paidOrder);

    return paidOrder;
  }

  public abstract void collectMoney(Order order) throws NoEnoughAmountException, SystemException;
}
