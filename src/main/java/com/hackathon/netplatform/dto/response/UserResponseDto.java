package com.hackathon.netplatform.dto.response;

import com.hackathon.netplatform.model.Role;
import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<Role> roles;
}
