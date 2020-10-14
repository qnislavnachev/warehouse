package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.services.BIService;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.exports.Report;
import com.warehouse.exports.ReportType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.NotSupportedException;

@RestController
public class BIController {
  private final BIService biService;

  public BIController(BIService biService) {
    this.biService = biService;
  }

  @GetMapping("/bi/users/{userId}/orders/items/count/avg")
  public ResponseEntity<Object> getUserOrdersProductsAverageAmount(@PathVariable Long userId) {
    try {
      Double amount = biService.getUserOrdersProductsAverageAmount(userId);
      return Response.ok(amount);

    } catch (UserNotFoundException e) {
      return Response.notFound(Error.of("Product with id %s does not exists", e.userId));
    }
  }

  @GetMapping("/bi/orders/items/{productId}/count/avg")
  public ResponseEntity<Object> getOrderedProductAverageAmount(@PathVariable Long productId) {
    try {
      Double amount = biService.getOrderedProductAverageAmount(productId);
      return Response.ok(amount);

    } catch (ProductNotFoundException e) {
      return Response.notFound(Error.of("Product with id %s does not exists", productId));
    }
  }

  @GetMapping("/bi/users/{userId}/orders/items/{productId}/count/avg")
  public ResponseEntity<Object> getOrderedProductAverageAmount(@PathVariable Long userId, @PathVariable Long productId) {

    try {
      Double amount = biService.getOrderedProductAverageAmount(userId, productId);
      return Response.ok(amount);

    } catch (ProductNotFoundException e) {
      return Response.notFound(Error.of("Product with id %s does not exists", e.productId));

    } catch (UserNotFoundException e) {
      return Response.notFound(Error.of("User with id %s does not exists", e.userId));
    }
  }

  @GetMapping("/bi/products/report")
  public ResponseEntity<Object> getProductsReport(@RequestParam String type) {
    try {
      ReportType reportType = ReportType.valueOf(type);

      Report report = biService.generateProductsReport(reportType);
      InputStreamResource resource = new InputStreamResource(report.getContent());

      return Response.ok(report.getFileName(), report.getContentLength(), resource);

    } catch (NotSupportedException | IllegalArgumentException e) {
      return Response.conflict(Error.of("Export type is not supported"));
    }
  }
}
