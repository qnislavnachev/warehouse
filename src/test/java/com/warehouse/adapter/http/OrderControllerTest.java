package com.warehouse.adapter.http;

import com.google.gson.Gson;
import com.warehouse.adapter.dao.order.OrderRepository;
import com.warehouse.adapter.dao.role.RoleRepository;
import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.dao.user.WalletRepository;
import com.warehouse.adapter.dao.warehouse.WarehouseRepository;
import com.warehouse.adapter.http.dto.CreateOrderRequest;
import com.warehouse.adapter.http.dto.OrderItemDto;
import com.warehouse.adapter.http.dto.PaymentType;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.Authority;
import com.warehouse.adapter.security.WebTokenGenerator;
import com.warehouse.core.*;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import com.warehouse.payment.PaymentMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderControllerTest {

  @Autowired
  WebTokenGenerator tokenGenerator;

  @Autowired
  WarehouseRepository warehouseRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WalletRepository walletRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  OrderRepository orderRepository;

  @Autowired
  MockMvc mockMvc;

  private final Gson gson = new Gson();

  private User admin;

  @BeforeEach
  void beforeEach() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    walletRepository.deposit(admin.getId(), 10000.0);
  }

  @Test
  public void createOrderSuccessful() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 5000.0));

    List<OrderItemDto> items = Collections.singletonList(new OrderItemDto(product.getId(), 3500.0));
    CreateOrderRequest orderRequest = new CreateOrderRequest(admin.getId(), items);
    String jsonContent = gson.toJson(orderRequest);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(post("/accounting/orders")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated());
  }

  @Test
  void userTriesToCreateOrderForAnotherUser() throws Exception {
    User firstUser = register(new User("::firstUser::", "firstUser@gmail.com", "::password::"));
    User secondUser = register(new User("::secondUser::", "secondUser@gmail.com", "::password::"));
    Product product = addProduct(new Product("Apples", 0.30, 5000.0));

    List<OrderItemDto> items = Collections.singletonList(new OrderItemDto(product.getId(), 3500.0));
    CreateOrderRequest secondUserOrderRequest = new CreateOrderRequest(secondUser.getId() + 200, items);
    String jsonContent = gson.toJson(secondUserOrderRequest);

    String firstUserBearerToken = authenticate(firstUser);

    mockMvc.perform(post("/accounting/orders")
            .header(HttpHeaders.AUTHORIZATION, firstUserBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.description").value("User doesn't have permission to create order for another user"));
  }

  @Test
  void orderedProductsWereNotFound() throws Exception {
    Long unexistingProductId = 0L;

    List<OrderItemDto> items = Collections.singletonList(new OrderItemDto(unexistingProductId, 20.0));
    CreateOrderRequest orderRequest = new CreateOrderRequest(admin.getId(), items);
    String jsonContent = gson.toJson(orderRequest);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(post("/accounting/orders")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.description").value("One or more products that were provided not found"));
  }

  @Test
  void createOrderWithInsufficientQuantityOfGivenProduct() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));

    List<OrderItemDto> items = Collections.singletonList(new OrderItemDto(product.getId(), product.getQuantity() + 1));
    CreateOrderRequest orderRequest = new CreateOrderRequest(admin.getId(), items);
    String jsonContent = gson.toJson(orderRequest);

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(post("/accounting/orders")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.description").value("Product with id " + product.getId() + " that has been ordered has insufficient quantity"));
  }

  @Test
  void getOrderById() throws Exception {
    User user = register(new User("::name::", "firstUser@gmail.com", "::password::"));

    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order order = orderRepository.create(new Order(user.getId(), orderItems, 50.0, false));

    String userBearerToken = authenticate(user);

    mockMvc.perform(get("/accounting/orders/{id}", order.getId())
            .header(HttpHeaders.AUTHORIZATION, userBearerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.ownerId").value(order.getOwnerId()))
            .andExpect(jsonPath("$.price").value(order.getPrice()))
            .andExpect(jsonPath("$.isPaid").value(order.isPaid()))
            .andExpect(jsonPath("$.orderItems", hasSize(1)))
            .andExpect(jsonPath("$.orderItems[0].productId").value(product.getId()))
            .andExpect(jsonPath("$.orderItems[0].quantity").value(20.0));
  }

  @Test
  void getOrderByIdOfAnotherUserAsAdmin() throws Exception {
    User user = register(new User("::name::", "firstUser@gmail.com", "::password::"));

    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order order = orderRepository.create(new Order(user.getId(), orderItems, 50.0, false));

    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/accounting/orders/{id}", order.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.ownerId").value(order.getOwnerId()))
            .andExpect(jsonPath("$.price").value(order.getPrice()))
            .andExpect(jsonPath("$.isPaid").value(order.isPaid()))
            .andExpect(jsonPath("$.orderItems", hasSize(1)))
            .andExpect(jsonPath("$.orderItems[0].productId").value(product.getId()))
            .andExpect(jsonPath("$.orderItems[0].quantity").value(20.0));
  }

  @Test
  void tryToGetUnexistingOrder() throws Exception {
    Long unexistingOrderId = 0L;
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/accounting/orders/{id}", unexistingOrderId)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.description").value("Order with id 0 was not found"));
  }

  @Test
  void anotherUserTriesToAccessYourOwnOrder() throws Exception {
    User firstUser = register(new User("::name::", "firstUser@gmail.com", "::password::"));
    User secondUser = register(new User("::name::", "secondUser@gmail.com", "::password::"));

    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order firstUserOrder = orderRepository.create(new Order(firstUser.getId(), orderItems, 50.0, false));

    String secondUserBearerToken = authenticate(secondUser);

    mockMvc.perform(get("/accounting/orders/{id}", firstUserOrder.getId())
            .header(HttpHeaders.AUTHORIZATION, secondUserBearerToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.description").value("The user does not have access to the given resource"));
  }

  @Test
  void payOrderSuccessfully() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order order = orderRepository.create(new Order(admin.getId(), orderItems, 50.0, false));

    String jsonContent = gson.toJson(new PaymentType(PaymentMethod.WALLET.toString()));
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(put("/accounting/orders/{id}/pay", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(order.getId()))
            .andExpect(jsonPath("$.ownerId").value(order.getOwnerId()))
            .andExpect(jsonPath("$.price").value(order.getPrice()))
            .andExpect(jsonPath("$.isPaid").value(true))
            .andExpect(jsonPath("$.orderItems", hasSize(1)))
            .andExpect(jsonPath("$.orderItems[0].productId").value(product.getId()))
            .andExpect(jsonPath("$.orderItems[0].quantity").value(20.0));
  }

  @Test
  void userTriesToPayOrderOwnedByAnotherUser() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order order = orderRepository.create(new Order(admin.getId(), orderItems, 50.0, false));

    User randomUser = register(new User("::name::", "dummy@gmail.com", "::password::"));

    String jsonContent = gson.toJson(new PaymentType(PaymentMethod.WALLET.toString()));
    String randomUserBearerToken = authenticate(randomUser);

    mockMvc.perform(put("/accounting/orders/{id}/pay", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, randomUserBearerToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.description").value("The user does not have access to the given resource"));
  }

  @Test
  void userTriesToPayAlreadyPaidOrder() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));

    Order order = orderRepository.create(new Order(admin.getId(), orderItems, 50.0, false));
    orderRepository.markAsPaid(order);

    String jsonContent = gson.toJson(new PaymentType(PaymentMethod.WALLET.toString()));
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(put("/accounting/orders/{id}/pay", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.description").value("The order with id 1 is already paid"));
  }

  @Test
  void userTriesToPayOrderWithNonSupportedPaymentType() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));

    Order order = orderRepository.create(new Order(admin.getId(), orderItems, 50.0, false));
    orderRepository.markAsPaid(order);

    String jsonContent = gson.toJson(new PaymentType("ONLINE"));
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(put("/accounting/orders/{id}/pay", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.description").value("Payment type is not supported yet"));
  }

  @Test
  void userTriesToPayUnexistingOrder() throws Exception {
    Long unexistingOrderId = 0L;

    String jsonContent = gson.toJson(new PaymentType(PaymentMethod.WALLET.toString()));
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(put("/accounting/orders/{id}/pay", unexistingOrderId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.description").value("Order with id 0 was not found"));
  }

  @Test
  void userDoesNotHaveEnoughMoneyToPay() throws Exception {
    Product product = addProduct(new Product("Apples", 0.30, 50.0));
    User user = register(new User("::name::", "dummy@gmail.com", "::password::"));

    List<OrderItem> orderItems = Collections.singletonList(new OrderItem(product.getId(), 20.0));
    Order order = orderRepository.create(new Order(user.getId(), orderItems, 50.0, false));

    String jsonContent = gson.toJson(new PaymentType(PaymentMethod.WALLET.toString()));
    String userBearerToken = authenticate(user);

    mockMvc.perform(put("/accounting/orders/{id}/pay", order.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent)
            .header(HttpHeaders.AUTHORIZATION, userBearerToken))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.description").value("Unable to process payment due to insufficient amount"));
  }


  private Product addProduct(Product product) {
    List<Product> products = warehouseRepository.addProducts(Collections.singletonList(product));

    return products.get(0);
  }

  private User register(User user, Role... roles) throws UserAlreadyExistsException {
    return userRepository.register(user, Arrays.asList(roles));
  }

  private User register(User user) throws UserAlreadyExistsException {
    return userRepository.register(user, Collections.emptyList());
  }

  private String authenticate(User user) {
    List<Authority> authorities = user.getRoles().stream().map(Authority::new).collect(Collectors.toList());
    AuthenticatedUser authUser = new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(), authorities);

    return "Bearer " + tokenGenerator.generate(authUser);
  }
}