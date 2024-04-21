package com.hackathon.netplatform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDto {
  @NotBlank private String email;
  @NotBlank
  @Size(min = 8, message = "Wrong username or password!")
  private String password;
}
