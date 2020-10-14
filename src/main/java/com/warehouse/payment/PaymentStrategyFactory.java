package com.warehouse.payment;

import com.warehouse.adapter.facades.OrderFacade;
import com.warehouse.adapter.facades.UserFacade;
import com.warehouse.adapter.facades.WarehouseStorageFacade;
import org.springframework.stereotype.Component;

import javax.transaction.NotSupportedException;

@Component
public class PaymentStrategyFactory {

  private final OrderFacade orderFacade;
  private final UserFacade userFacade;
  private final WarehouseStorageFacade warehouseFacade;

  public PaymentStrategyFactory(OrderFacade orderFacade, WarehouseStorageFacade warehouseFacade, UserFacade userFacade) {
    this.orderFacade = orderFacade;
    this.userFacade = userFacade;
    this.warehouseFacade = warehouseFacade;
  }

  public PaymentStrategy get(PaymentMethod paymentMethod) throws NotSupportedException {

    if (paymentMethod.equals(PaymentMethod.WALLET)) {
      return new WalletPaymentStrategy(orderFacade, warehouseFacade, userFacade);
    }

    if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
      return new CreditCardPaymentStrategy(orderFacade, warehouseFacade);
    }

    throw new NotSupportedException();
  }
}
