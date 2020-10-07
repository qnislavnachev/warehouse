package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.services.BIService;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.SystemException;
import com.warehouse.core.exceptions.UserNotFoundException;
import com.warehouse.exports.ExportType;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

  @GetMapping("/bi/products/export")
  public ResponseEntity<Object> getProductsExport(@RequestParam String type) {
    try {
      ExportType exportType = ExportType.valueOf(type);

      //TODO: the file should be deleted
      File file = biService.generateProductsReport(exportType);
      InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

      return Response.ok(file.length(), resource);

    } catch (NotSupportedException | IllegalArgumentException e) {
      return Response.conflict(Error.of("Export type is not supported"));

    } catch (SystemException | FileNotFoundException e) {
      return Response.serverError("The generation of the export failed due to internal server error");
    }
  }
}
