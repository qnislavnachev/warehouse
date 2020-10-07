package com.warehouse.payment;

import com.warehouse.adapter.dao.order.OrderFacade;
import com.warehouse.adapter.dao.warehouse.WarehouseStorageFacade;
import com.warehouse.core.Order;
import com.warehouse.core.exceptions.*;

abstract class AppPaymentStrategy implements PaymentStrategy {
  private final OrderFacade orderFacade;
  private final WarehouseStorageFacade warehouseFacade;

  protected AppPaymentStrategy(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade) {
    this.orderFacade = orderFacade;
    this.warehouseFacade = warehouseFacade;
  }

  @Override
  public Order pay(Order order) throws InvalidPaymentSourceException, NoEnoughAmountException, SystemException {
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

  public abstract void collectMoney(Order order) throws InvalidPaymentSourceException, NoEnoughAmountException;
}
