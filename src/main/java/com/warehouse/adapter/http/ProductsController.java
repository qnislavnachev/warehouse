package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.Error;
import com.warehouse.adapter.http.dto.NewProductDto;
import com.warehouse.adapter.http.dto.ProductDto;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.services.ProductsService;
import com.warehouse.core.Product;
import com.warehouse.core.Role;
import com.warehouse.core.exceptions.ProductNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductsController {
  private final ProductsService productsService;

  public ProductsController(ProductsService productsService) {
    this.productsService = productsService;
  }

  @PostMapping("/warehouse/products")
  public ResponseEntity<Object> loadProducts(@RequestBody List<NewProductDto> newProducts) {
    List<Product> products = newProducts.stream()
            .map(dto -> new Product(dto.name, dto.price, dto.quantity))
            .collect(Collectors.toList());

    productsService.loadProducts(products);
    return Response.created();
  }

  @GetMapping("/warehouse/products")
  public ResponseEntity<Object> getProducts() {
    List<Product> products = productsService.getAllProducts();

    return Response.ok(ProductDto.adapt(products));
  }

  @GetMapping("/warehouse/products/{id}")
  public ResponseEntity<Object> getProduct(@PathVariable Long id) {
    try {
      Product product = productsService.getProduct(id);
      return Response.ok(ProductDto.adapt(product));

    } catch (ProductNotFoundException e) {
      return Response.notFound(Error.of("Product with id %s was not found", e.productId));
    }
  }
}
