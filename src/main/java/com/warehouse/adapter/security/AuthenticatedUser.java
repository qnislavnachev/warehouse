package com.warehouse.adapter.security;

import com.warehouse.core.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthenticatedUser implements UserDetails {
  private final Long id;
  private final String email;
  private final String password;
  private final List<GrantedAuthority> authorities;

  public AuthenticatedUser(Long id, String email, String password, List<GrantedAuthority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public Long getId() {
    return id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public boolean isAdmin() {
    return authorities.stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + Role.ADMIN_ROLE));
  }

  public static AuthenticatedUser getCurrentUser() {
    return (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
