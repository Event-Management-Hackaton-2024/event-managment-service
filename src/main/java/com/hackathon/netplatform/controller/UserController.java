package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @Operation(summary = "Get user from token")
  @GetMapping("/{token}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto getUserFromToken(@PathVariable("token") String token) {
    return userService.getUserByToken(token);
  }

  @Operation(summary = "Delete user")
  @DeleteMapping("/{email}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("email") String email) {
    userService.deleteUser(email);
  }
}
