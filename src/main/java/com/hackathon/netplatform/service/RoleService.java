package com.hackathon.netplatform.service;

import com.hackathon.netplatform.model.Role;

public interface RoleService {
  void createRolesInDatabase();

  Role getRole(String name);
}
