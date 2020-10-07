package com.warehouse.adapter.services.security;

import com.warehouse.adapter.dao.user.UserFacade;
import com.warehouse.adapter.security.AuthenticatedUser;
import com.warehouse.adapter.security.Authority;
import com.warehouse.core.User;
import com.warehouse.core.exceptions.UserNotFoundException;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthenticationService implements UserDetailsService {
  private final UserFacade userFacade;

  public UserAuthenticationService(UserFacade userFacade) {
    this.userFacade = userFacade;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    try {
      User user = userFacade.getUser(email);

      List<Authority> authorities = user.getRoles().stream()
              .map(Authority::new)
              .collect(Collectors.toList());

      return new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(), authorities);
    } catch (UserNotFoundException e) {
      throw new UsernameNotFoundException(String.format("User with email %s was not found", e.userEmail));
    }
  }
}
