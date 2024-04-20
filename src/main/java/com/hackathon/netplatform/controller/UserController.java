package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EditUserRequestDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

  @Operation(summary = "Edit user")
  @PutMapping("/{email}")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto editUser(@PathVariable("email") String email, @Valid @RequestBody EditUserRequestDto editUserRequestDto) {
    return userService.editUser(email, editUserRequestDto);
  }

  @Operation(summary = "Follow user")
  @PutMapping("/follow/{followerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto addFollower(@PathVariable("followerId") UUID followerId) {
    return userService.addFollower(followerId);
  }

  @Operation(summary = "Unfollow user")
  @PutMapping("/unfollow/{followerId}")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto unfollowUser(@PathVariable("followerId") UUID followerId) {
    return userService.unfollowUser(followerId);
  }
}
