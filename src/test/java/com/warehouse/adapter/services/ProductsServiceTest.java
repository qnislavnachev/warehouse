package com.warehouse.adapter.services;

import com.warehouse.MockedTest;
import com.warehouse.core.Product;
import com.warehouse.core.events.ProductsLoadedEvent;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.eventbus.EventBus;
import com.warehouse.facades.WarehouseStorageFacade;
import org.jmock.Expectations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductsServiceTest extends MockedTest {
  EventBus eventBus = mock(EventBus.class);
  WarehouseStorageFacade warehouseStorageFacade = mock(WarehouseStorageFacade.class);

  ProductsService productsService = new ProductsService(eventBus, warehouseStorageFacade);


  @Test
  void loadProducts() {
    List<Product> products = Arrays.asList(
            new Product("Chocolate", 1.75, 20.0),
            new Product("Popcorn", 0.60, 20.0)
    );

    expect(new Expectations() {{
      oneOf(warehouseStorageFacade).addProducts(products);

      oneOf(eventBus).publish(with(any(ProductsLoadedEvent.class)));
    }});

    productsService.loadProducts(products);
  }

  @Test
  void getProductById() throws Exception {
    Product expectedProduct = new Product(1L, "Chocolate", 1.75, 20.0);

    expect(new Expectations() {{
      oneOf(warehouseStorageFacade).getProduct(1L);
      will(returnValue(expectedProduct));
    }});

    Product product = productsService.getProduct(1L);

    assertThat(product, is(expectedProduct));
  }

  @Test
  void tryToGetProductThatDoesNotExists() throws Exception {

    expect(new Expectations() {{
      oneOf(warehouseStorageFacade).getProduct(1L);
      will(throwException(new ProductNotFoundException(1L)));
    }});

    assertThrows(ProductNotFoundException.class, () -> {
      productsService.getProduct(1L);
    });
  }

  @Test
  void getAllProducts() {
    List<Product> expectedProducts = Arrays.asList(
            new Product("Chocolate", 1.75, 20.0),
            new Product("Popcorn", 0.60, 20.0)
    );

    expect(new Expectations() {{
      oneOf(warehouseStorageFacade).getProducts();
      will(returnValue(expectedProducts));
    }});

    List<Product> allProducts = productsService.getAllProducts();
    assertThat(allProducts, is(expectedProducts));
  }
}