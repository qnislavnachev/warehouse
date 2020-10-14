package com.warehouse.adapter.dao.warehouse;

import com.warehouse.core.Order;
import com.warehouse.core.OrderItem;
import com.warehouse.core.Product;
import com.warehouse.core.Reservation;
import com.warehouse.core.exceptions.NoEnoughQuantityException;
import com.warehouse.core.exceptions.ProductNotFoundException;
import com.warehouse.core.exceptions.ProductsWereNotFoundException;
import com.warehouse.core.exceptions.UnableToSellStockException;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Repository
class PersistentWarehouseRepository implements WarehouseRepository {
  private final ProductDatabase database;

  public PersistentWarehouseRepository(ProductDatabase database) {
    this.database = database;
  }

  @Override
  public Product getProduct(Long id) throws ProductNotFoundException {
    Optional<ProductEntity> possibleEntity = database.findById(id);

    if (!possibleEntity.isPresent()) {
      throw new ProductNotFoundException(id);
    }

    return adapt(possibleEntity.get());
  }

  @Override
  public List<Product> getProducts() {
    List<ProductEntity> productEntities = database.findAll();

    return productEntities.stream()
            .map(it -> adapt(it))
            .collect(Collectors.toList());
  }

  @Override
  public List<Product> addProducts(List<Product> products) {
    List<Product> savedProducts = new ArrayList<>();

    products.forEach(product -> {
      ProductEntity productEntity = database.findByName(product.getName());

      if (isNull(productEntity)) {
        productEntity = ProductEntity.from(product);
      } else {
        productEntity.addQuantity(product.getQuantity());
      }

      database.save(productEntity);
      savedProducts.add(adapt(productEntity));
    });

    return savedProducts;
  }

  @Override
  public Reservation reserveProducts(ReservationRequest reservationRequest) throws NoEnoughQuantityException, ProductsWereNotFoundException {
    Set<Long> productsIds = reservationRequest.getProductsIds();

    if (productsIds.isEmpty()) {
      return new Reservation();
    }

    List<ProductEntity> productEntities = database.findAllById(reservationRequest.getProductsIds());

    if (reservationRequest.getProductsIds().size() != productEntities.size()) {
      //TODO: provide info for the missing products
      throw new ProductsWereNotFoundException();
    }

    List<Product> reservedProducts = new ArrayList<>();

    for (ProductEntity entity : productEntities) {
      Double quantity = reservationRequest.getQuantity(entity.getId());
      boolean isReservationSuccessful = entity.reserve(quantity);

      if (!isReservationSuccessful) {
        throw new NoEnoughQuantityException(entity.getId(), entity.getQuantity());
      }

      reservedProducts.add(new Product(entity.getId(), entity.getName(), entity.getPrice(), quantity));
    }

    database.saveAll(productEntities);

    return new Reservation(reservedProducts);
  }

  @Override
  public void sellOrderStock(Order order) throws NoEnoughQuantityException, UnableToSellStockException {
    if (order.getOrderItems().isEmpty()) {
      return;
    }

    Map<Long, Double> productsQuantities = order.getOrderItems().stream()
            .collect(Collectors.toMap(OrderItem::getProductId, OrderItem::getQuantity));

    Set<Long> productsIds = productsQuantities.keySet();
    List<ProductEntity> productEntities = database.findAllById(productsIds);

    if (productsIds.size() != productEntities.size()) {
      throw new UnableToSellStockException();
    }

    for (ProductEntity entity : productEntities) {
      Double productQuantity = productsQuantities.getOrDefault(entity.getId(), 0.0);

      boolean isSuccessful = entity.sellStock(productQuantity);

      if (!isSuccessful) {
        throw new NoEnoughQuantityException(entity.getId(), entity.getQuantity());
      }
    }

    database.saveAll(productEntities);
  }

  private Product adapt(ProductEntity entity) {
    return new Product(entity.getId(), entity.getName(), entity.getPrice(), entity.getQuantity());
  }
}
