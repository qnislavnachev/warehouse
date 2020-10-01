package com.warehouse.adapter.dao.bi;

import com.warehouse.core.Product;
import com.warehouse.core.User;

public interface BIRepository {

  /**
   * It will provide the average number of how many products
   * user usually order in his orders.
   *
   * @param user  the user for which it will fetch the statistic
   *
   * @return the statically average number of the user ordered products
   */
  Double getUserOrdersProductsAverageAmount(User user);

  /**
   * It will provide the average number of how much quantity of given product
   * users usually order in their orders.
   *
   * @param product  the product for which it will fetch the statistic
   *
   * @return the statically average number of the given product ordered in all users orders
   */
  Double getOrderedProductAverageAmount(Product product);

  /**
   * It will provide the average number of how much quantity of given product
   * user usually order in his orders
   *
   * @param user      the user for which it will fetch the statistic
   * @param product   the product for which it will fetch the statistic
   *
   * @return the statically average number of given product quantity for given user
   */
  Double getOrderedProductAverageAmount(User user, Product product);
}
