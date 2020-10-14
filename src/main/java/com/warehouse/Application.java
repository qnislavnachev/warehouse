package com.warehouse;

import com.warehouse.adapter.facades.UserFacade;
import com.warehouse.adapter.http.filters.SecurityFilter;
import com.warehouse.adapter.security.WebTokenGenerator;
import com.warehouse.adapter.security.WebTokenParser;
import com.warehouse.adapter.security.jwt.JwtGenerator;
import com.warehouse.adapter.security.jwt.JwtParser;
import com.warehouse.adapter.services.security.UserAuthenticationService;
import com.warehouse.core.Role;
import com.warehouse.eventbus.AppEventBus;
import com.warehouse.eventbus.AppEventManager;
import com.warehouse.eventbus.EventBus;
import com.warehouse.eventbus.listeners.EmailNotifier;
import com.warehouse.eventbus.listeners.LoggingListener;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableJpaRepositories
public class Application extends WebSecurityConfigurerAdapter {
  private final ApplicationContext context;
  private final Set<String> unauthorizedEndpoints;

  //TODO: this should be removed from here
  private final static String SECRET_KEY = "::iani-secret-key::";

  public Application(ApplicationContext context) {
    this.context = context;
    this.unauthorizedEndpoints = new HashSet<>(Collections.singletonList("/auth"));
  }

  @Override
  protected AuthenticationManager authenticationManager() throws Exception {
    return super.authenticationManager();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    SecurityFilter securityFilter = context.getBean(SecurityFilter.class);

    http.csrf().disable()
            .authorizeRequests().antMatchers("/auth").permitAll()
            .and()
            .authorizeRequests().antMatchers("/bi/**").hasRole(Role.ADMIN_ROLE)
            .and()
            .authorizeRequests().antMatchers("/roles").hasRole(Role.ADMIN_ROLE)
            .and()
            .authorizeRequests().antMatchers(HttpMethod.POST, "/users", "/warehouse/products").hasRole(Role.ADMIN_ROLE)
            .anyRequest().permitAll()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  public UserDetailsService userDetailsServiceBean() throws Exception {
    UserFacade userFacade = context.getBean(UserFacade.class);
    return new UserAuthenticationService(userFacade);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }


  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public EventBus getEventBus() {
    LoggingListener loggingListener = context.getBean(LoggingListener.class);
    EmailNotifier emailNotifier = context.getBean(EmailNotifier.class);

    AppEventManager eventManager = new AppEventManager();
    eventManager.register(loggingListener);
    eventManager.register(emailNotifier);

    return new AppEventBus(eventManager);
  }

  @Bean
  public WebTokenGenerator tokenGenerator() {
    return new JwtGenerator(SECRET_KEY, 10L * 60L, SignatureAlgorithm.HS256);
  }

  @Bean
  public WebTokenParser tokenParser() {
    return new JwtParser(SECRET_KEY);
  }

  @Bean
  public SecurityFilter securityFilter() {
    return new SecurityFilter(unauthorizedEndpoints, tokenParser());
  }
}
