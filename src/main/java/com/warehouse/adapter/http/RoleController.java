package com.warehouse.adapter.http;

import com.warehouse.adapter.http.dto.NewRoleDto;
import com.warehouse.adapter.http.dto.Response;
import com.warehouse.adapter.http.dto.RoleDto;
import com.warehouse.adapter.services.RoleService;
import com.warehouse.core.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoleController {
  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @PostMapping("/roles")
  public ResponseEntity<Object> addRole(@RequestBody NewRoleDto newRole) {
    roleService.addRole(new Role(newRole.name));

    return Response.created();
  }

  @GetMapping("/roles")
  public ResponseEntity<Object> getAllRoles() {
    List<Role> roles = roleService.getAllRoles();

    return Response.ok(RoleDto.adapt(roles));
  }
}
