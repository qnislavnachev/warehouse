package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.*;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.services.OrderService;
import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.exceptions.*;
import com.warehouse.payment.PaymentMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.NotSupportedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("/accounting/orders")
  public ResponseEntity<Object> createOrder(@RequestBody CreateOrderRequest request) {
    AuthenticatedUser currentUser = AuthenticatedUser.getCurrentUser();

    if (!currentUser.getId().equals(request.ownerId)) {
      return Response.forbidden(Error.of("User doesn't have permission to create order for another user"));
    }

    try {
      List<OrderItem> orderItems = adapt(request.orderItems);
      Order order = orderService.createOrder(request.ownerId, orderItems);

      return Response.created(OrderDto.adapt(order));

    } catch (NoEnoughQuantityException e) {
      return Response.conflict(Error.of("Product with id %s that has been ordered has insufficient quantity", e.productId));

    } catch (ProductsWereNotFoundException e) {
      return Response.conflict(Error.of("One or more products that were provided not found"));
    }
  }

  @GetMapping("/accounting/orders/{id}")
  public ResponseEntity<Object> getOrder(@PathVariable Long id) {
    AuthenticatedUser currentUser = AuthenticatedUser.getCurrentUser();

    try {
      Order order = orderService.getOrder(id);

      if (order.isOwnedBy(currentUser) || currentUser.isAdmin()) {
        return Response.ok(OrderDto.adapt(order));
      }

      return Response.forbidden(Error.of("The user does not have access to the given resource"));

    } catch (OrderNotFoundException e) {
      return Response.notFound(Error.of("Order with id %s was not found", e.orderId));
    }
  }

  @PutMapping("/accounting/orders/{id}/pay")
  public ResponseEntity<Object> payOrder(@PathVariable Long id, @RequestBody PaymentType paymentType) {

    try {
      AuthenticatedUser currentUser = AuthenticatedUser.getCurrentUser();
      PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType.paymentType);

      Order order = orderService.getOrder(id);

      if (!order.isOwnedBy(currentUser) && !currentUser.isAdmin()) {
        return Response.forbidden(Error.of("The user does not have access to the given resource"));
      }

      Order paidOrder = orderService.payOrder(order, paymentMethod);
      return Response.ok(OrderDto.adapt(paidOrder));

    } catch (NotSupportedException | IllegalArgumentException e) {
      return Response.badRequest(Error.of("Payment type is not supported yet"));

    } catch (WalletNotFoundException e) {
      return Response.notFound(Error.of("Wallet with owner id %s was not found", e.ownerId));

    } catch (OrderNotFoundException e) {
      return Response.notFound(Error.of("Order with id %s was not found", e.orderId));

    } catch (NoEnoughAmountException e) {
      return Response.conflict(Error.of("Unable to process payment due to insufficient amount"));

    } catch (SystemException e) {
      return Response.serverError(Error.of("Unable to process payment due to internal system error"));
    }
  }

  private List<OrderItem> adapt(List<OrderItemDto> items) {
    return items.stream()
            .map(it -> new OrderItem(it.productId, it.quantity))
            .collect(Collectors.toList());
  }
}
