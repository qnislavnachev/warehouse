package com.warehouse.adapter.http;

import com.google.gson.Gson;
import com.warehouse.adapter.dao.role.RoleRepository;
import com.warehouse.adapter.dao.user.UserRepository;
import com.warehouse.adapter.http.dto.DepositRequest;
import com.warehouse.adapter.http.dto.RegisterUserRequest;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.WebTokenGenerator;
import com.warehouse.core.Role;
import com.warehouse.core.User;
import com.warehouse.core.Wallet;
import com.warehouse.core.exceptions.UserAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

  @Autowired
  WebTokenGenerator tokenGenerator;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  MockMvc mockMvc;

  private final Gson gson = new Gson();

  @Test
  void registerUserSuccessfully() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    Role randomRole = roleRepository.add(new Role("::random-role::"));

    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    RegisterUserRequest registerUserRequest = new RegisterUserRequest("::name::", "dummy@gmail.com", "::password::", Collections.singletonList(randomRole.getId()));
    String jsonContent = gson.toJson(registerUserRequest);

    mockMvc.perform(post("/users")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(notNullValue()))
            .andExpect(jsonPath("$.name").value(registerUserRequest.name))
            .andExpect(jsonPath("$.email").value(registerUserRequest.email))
            .andExpect(jsonPath("$.walletBalance").value(0.0))
            .andExpect(jsonPath("$.roles").isArray())
            .andExpect(jsonPath("$.roles", hasSize(1)))
            .andExpect(jsonPath("$.roles", hasItem(randomRole.getName())));
  }

  @Test
  void noneAdminUserTriesToRegisterNewUser() throws Exception {
    User user = register(new User("::username::", "user@gmail.com", "::password::"));
    String userBearerToken = authenticate(user);

    RegisterUserRequest registerUserRequest = new RegisterUserRequest("::iani::", "dummy@gmail.com", "::password::", Collections.emptyList());
    String jsonContent = gson.toJson(registerUserRequest);

    mockMvc.perform(post("/users")
            .header(HttpHeaders.AUTHORIZATION, userBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isForbidden());
  }

  @Test
  void tryToRegisterUserWithTheSameEmail() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    register(new User("::name::", "dummy@gmail.com", "::password::"));

    RegisterUserRequest registerUserRequest = new RegisterUserRequest("::name::", "dummy@gmail.com", "::password::", Collections.emptyList());
    String jsonContent = gson.toJson(registerUserRequest);

    mockMvc.perform(post("/users")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.description").value("User with email dummy@gmail.com already exists"));
  }

  @Test
  void tryToCreateUserWithUnexistingRole() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::adminPassword::"), adminRole);
    String adminBearerToken = authenticate(admin);

    RegisterUserRequest registerUserRequest = new RegisterUserRequest("::name::", "dummy@gmail.com", "::password::", Collections.singletonList(0L));
    String jsonContent = gson.toJson(registerUserRequest);

    mockMvc.perform(post("/users")
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonContent))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.description").value("Some of the roles that were provided dont exists"));
  }

  @Test
  void getUserByIdSuccessfully() throws Exception {
    Role role = roleRepository.add(new Role("::random-role::"));
    User user = register(new User("::name::", "dummy@gmail.com", "::password::"), role);
    String userBearerToken = authenticate(user);

    mockMvc.perform(get("/users/{id}", user.getId())
            .header(HttpHeaders.AUTHORIZATION, userBearerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.name").value(user.getName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.walletBalance").value(user.getWallet().getBalance()))
            .andExpect(jsonPath("$.roles", hasSize(1)))
            .andExpect(jsonPath("$.roles", hasItem(role.getName())));
  }

  @Test
  void getUserByIdSuccessfullyAsAdmin() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    Role randomRole = roleRepository.add(new Role("::random-role::"));
    User user = register(new User("::name::", "dummy@gmail.com", "::password::"), randomRole);

    mockMvc.perform(get("/users/{id}", user.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(user.getId()))
            .andExpect(jsonPath("$.name").value(user.getName()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.walletBalance").value(user.getWallet().getBalance()))
            .andExpect(jsonPath("$.roles", hasSize(1)))
            .andExpect(jsonPath("$.roles", hasItem(randomRole.getName())));
  }

  @Test
  void userTryToAccessAnotherUserProfile() throws Exception {
    User firstUser = register(new User("::username::", "firstUser@gmail.com", "::password::"));
    User secondUser = register(new User("::username::", "secondUser@gmail.com", "::password::"));

    String fistUserBearerToken = authenticate(firstUser);

    mockMvc.perform(get("/users/{id}", secondUser.getId())
            .header(HttpHeaders.AUTHORIZATION, fistUserBearerToken))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.description").value("The user does not have access to the given resource"));
  }

  @Test
  void tryToGetUnexistingUser() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    mockMvc.perform(get("/users/{id}", 0L)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.description").value("User with id 0 was not found"));
  }

  @Test
  void depositToUserWallet() throws Exception {
    User user = register(new User("::name::", "dummy@gmail.com", "::password::"));
    String userBearerToken = authenticate(user);

    DepositRequest depositRequest = new DepositRequest(50.0);
    String depositRequestContent = gson.toJson(depositRequest);

    mockMvc.perform(put("/users/{id}/wallet/deposit", user.getId())
            .header(HttpHeaders.AUTHORIZATION, userBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(depositRequestContent))
            .andExpect(status().isOk());


    Wallet userWallet = userRepository.getUser(user.getId()).getWallet();

    assertThat(userWallet.getBalance(), is(50.0));
  }

  @Test
  void adminDepositsToUserWallet() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    User user = register(new User("::name::", "dummy@gmail.com", "::password::"));

    DepositRequest depositRequest = new DepositRequest(34.0);
    String depositRequestContent = gson.toJson(depositRequest);

    mockMvc.perform(put("/users/{id}/wallet/deposit", user.getId())
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(depositRequestContent))
            .andExpect(status().isOk());

    Wallet userWallet = userRepository.getUser(user.getId()).getWallet();

    assertThat(userWallet.getBalance(), is(34.0));
  }

  @Test
  void depositToUnexistingUserWallet() throws Exception {
    Role adminRole = roleRepository.add(new Role("admin"));
    User admin = register(new User("admin", "admin@gmail.com", "::password::"), adminRole);
    String adminBearerToken = authenticate(admin);

    DepositRequest depositRequest = new DepositRequest(34.0);
    String depositRequestContent = gson.toJson(depositRequest);

    mockMvc.perform(put("/users/{id}/wallet/deposit", 0L)
            .header(HttpHeaders.AUTHORIZATION, adminBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(depositRequestContent))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.description").value("Wallet with owner id 0 was not found"));
  }

  @Test
  void userTryToDepositToAnotherUserWallet() throws Exception {
    User firstUser = register(new User("::username::", "firstUser@gmail.com", "::password::"));
    User secondUser = register(new User("::username::", "secondUser@gmail.com", "::password::"));

    String fistUserBearerToken = authenticate(firstUser);

    DepositRequest depositRequest = new DepositRequest(50.0);
    String depositRequestContent = gson.toJson(depositRequest);

    mockMvc.perform(put("/users/{id}/wallet/deposit", secondUser.getId())
            .header(HttpHeaders.AUTHORIZATION, fistUserBearerToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(depositRequestContent))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.description").value("The user does not have access to the given resource"));
  }

  private User register(User user, Role... roles) throws UserAlreadyExistsException {
    return userRepository.register(user, Arrays.asList(roles));
  }

  private User register(User user) throws UserAlreadyExistsException {
    return userRepository.register(user, Collections.emptyList());
  }

  private String authenticate(User user) {
    List<GrantedAuthority> authorities = user.getRoles().stream().map(it -> new SimpleGrantedAuthority("ROLE_" + it.getName())).collect(Collectors.toList());
    AuthenticatedUser authUser = new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(), authorities);

    return "Bearer " + tokenGenerator.generate(authUser);
  }
}