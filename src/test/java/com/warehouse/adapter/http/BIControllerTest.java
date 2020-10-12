package com.warehouse.adapter.http;

import com.warehouse.adapter.dao.order.OrderFacade;
import com.warehouse.adapter.dao.role.RoleRepository;
import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.Authority;
import com.warehouse.adapter.security.WebTokenGenerator;
import com.warehouse.core.OrderItem;
import com.warehouse.core.Product;
import com.warehouse.core.Role;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BIControllerTest {

  @Autowired
  OrderFacade orderFacade;

  @Autowired
  WebTokenGenerator tokenGenerator;

  @Autowired
  WarehouseRepository warehouseRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MockMvc mockMvc;

  private User admin;
  private Role adminRole;

  @BeforeAll
  void beforeAll() throws Exception {
    adminRole = roleRepository.add(new Role("admin"));
    admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
  }

  @Test
  void fetchUserAverageCountOfHisOrderItems() throws Exception {
    Product chocolate = addProduct(new Product("Chocolate", 1.95, 5000.0));
    Product bananas = addProduct(new Product("Bananas", 0.75, 5000.0));

    List<OrderItem> firstOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 15.0),
            new OrderItem(chocolate.getId(), 20.0)
    );

    orderFacade.create(admin.getId(), firstOrderItems);

    List<OrderItem> secondOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 5.0),
            new OrderItem(chocolate.getId(), 29.0)
    );

    orderFacade.create(admin.getId(), secondOrderItems);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/bi/users/{userId}/orders/items/count/avg", admin.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("17.25"));
  }

  @Test
  void fetchProductAverageCountFromAllOrders() throws Exception {
    Product chocolate = addProduct(new Product("Chocolate", 1.95, 5000.0));
    Product bananas = addProduct(new Product("Bananas", 0.75, 5000.0));

    List<OrderItem> firstOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 15.0),
            new OrderItem(chocolate.getId(), 20.0)
    );

    orderFacade.create(admin.getId(), firstOrderItems);

    List<OrderItem> secondOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 5.0),
            new OrderItem(chocolate.getId(), 29.0)
    );

    orderFacade.create(admin.getId(), secondOrderItems);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/bi/orders/items/{productId}/count/avg", bananas.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("10.0"));

    mockMvc.perform(get("/bi/orders/items/{productId}/count/avg", chocolate.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("24.5"));
  }

  @Test
  void fetchProductAverageCountFromGivenUserOrders() throws Exception {
    User anotherAdmin = register(new User("::random-name::", "::random-email::", "::random-password::"), adminRole);
    Product chocolate = addProduct(new Product("Chocolate", 1.95, 5000.0));
    Product bananas = addProduct(new Product("Bananas", 0.75, 5000.0));

    List<OrderItem> firstOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 15.0),
            new OrderItem(chocolate.getId(), 20.0)
    );

    orderFacade.create(admin.getId(), firstOrderItems);

    List<OrderItem> secondOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 5.0),
            new OrderItem(chocolate.getId(), 29.0)
    );

    orderFacade.create(admin.getId(), secondOrderItems);

    List<OrderItem> thirdOrderItems = Arrays.asList(
            new OrderItem(bananas.getId(), 1.0),
            new OrderItem(chocolate.getId(), 29.0)
    );
    orderFacade.create(anotherAdmin.getId(), thirdOrderItems);

    List<OrderItem> fourthOrderItems = Arrays.asList(new OrderItem(bananas.getId(), 5.0));
    orderFacade.create(anotherAdmin.getId(), fourthOrderItems);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/bi/users/{userId}/orders/items/{productId}/count/avg", admin.getId(), bananas.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("10.0"));

    mockMvc.perform(get("/bi/users/{userId}/orders/items/{productId}/count/avg", anotherAdmin.getId(), bananas.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("3.0"));
  }

  private User register(User user, Role... roles) throws UserAlreadyExistsException {
    return userRepository.register(user, Arrays.asList(roles));
  }

  private Product addProduct(Product product) {
    List<Product> addedProducts = warehouseRepository.addProducts(Collections.singletonList(product));

    return addedProducts.get(0);
  }

  private String authenticate(User user) {
    List<Authority> authorities = user.getRoles().stream().map(Authority::new).collect(Collectors.toList());
    AuthenticatedUser authUser = new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(), authorities);

    return "Bearer " + tokenGenerator.generate(authUser);
  }
}