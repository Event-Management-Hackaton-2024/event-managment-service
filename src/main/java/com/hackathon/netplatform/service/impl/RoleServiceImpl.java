package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.model.Role;
import com.hackathon.netplatform.model.RoleName;
import com.hackathon.netplatform.repository.RoleRepository;
import com.hackathon.netplatform.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public void createRolesInDatabase() {
        if (roleRepository.count() == 0) {
            Role admin = setRole(RoleName.ADMIN.name());
            Role user = setRole(RoleName.USER.name());
            Role eventCreator = setRole(RoleName.EVENT_CREATOR.name());
            Role moderator = setRole(RoleName.MODERATOR.name());
            roleRepository.saveAll(List.of(admin, user, eventCreator, moderator));
        }
    }

    public Role setRole(String name) {
        Role admin = new Role();
        admin.setName(name);
        return admin;
    }

    public Role getRole(String name) {
        return roleRepository.findByName(name).orElse(null);
    }
}
