package com.hackathon.netplatform.dto.response;

import com.hackathon.netplatform.model.Role;
import java.util.List;
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
    private List<UserResponseDto> followers;
    private List<InterestResponseDto> skills;
    private List<InterestResponseDto> interests;
    private String phoneNumber;
}
