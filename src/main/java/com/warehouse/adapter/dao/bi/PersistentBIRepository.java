package com.warehouse.adapter.dao.bi;

import com.warehouse.core.Product;
import com.warehouse.core.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Repository
class PersistentBIRepository implements BIRepository {

  @PersistenceContext
  private EntityManager entityManager;

  @Override
  public Double getUserOrdersProductsAverageAmount(User user) {
    String query = "SELECT AVG(item.quantity) FROM OrderItemEntity item,  OrderEntity userOrder " +
            "WHERE item.order.id = userOrder.id AND userOrder.ownerId = :userId";

    TypedQuery<Double> typedQuery = entityManager.createQuery(query, Double.class);
    typedQuery.setParameter("userId", user.getId());

    return typedQuery.getSingleResult();
  }

  @Override
  public Double getOrderedProductAverageAmount(Product product) {
    String query = "SELECT AVG(quantity) FROM OrderItemEntity WHERE productId = :productId";

    TypedQuery<Double> typedQuery = entityManager.createQuery(query, Double.class);
    typedQuery.setParameter("productId", product.getId());

    return typedQuery.getSingleResult();
  }

  @Override
  public Double getOrderedProductAverageAmount(User user, Product product) {
    String query = "SELECT AVG(item.quantity) FROM OrderItemEntity item, OrderEntity userOrder " +
            "WHERE item.order.id = userOrder.id AND item.productId = :productId AND userOrder.ownerId = :userId";

    TypedQuery<Double> typedQuery = entityManager.createQuery(query, Double.class);
    typedQuery.setParameter("productId", product.getId());
    typedQuery.setParameter("userId", user.getId());

    return typedQuery.getSingleResult();
  }
}
