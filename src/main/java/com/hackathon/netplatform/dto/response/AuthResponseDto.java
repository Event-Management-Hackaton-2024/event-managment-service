package com.hackathon.netplatform.dto.response;

import com.hackathon.netplatform.model.Role;
import lombok.Data;

import java.util.Set;

@Data
public class AuthResponseDto {
    private String token;
    private String email;
    private Set<Role> role;
}
