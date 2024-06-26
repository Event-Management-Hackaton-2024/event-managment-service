package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.EditUserRequestDto;
import com.hackathon.netplatform.dto.request.InterestsIdsRequest;
import com.hackathon.netplatform.dto.response.ImageResponseDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.service.ImageService;
import com.hackathon.netplatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final ImageService imageService;

  @Operation(summary = "Get user from token")
  @GetMapping("/{email}")
  @ResponseStatus(HttpStatus.OK)
  public UserResponseDto getUserByEmail(@PathVariable("email") String email) {
    return userService.getUserResponse(email);
  }

  @Operation(summary = "Get all user")
  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> getAllUsers() {
    return userService.gerAllUsersResponse();
  }

  @Operation(summary = "Get all users by pagination")
  @GetMapping("/{offset}/{pageSize}")
  @ResponseStatus(HttpStatus.OK)
  public Page<UserResponseDto> getAllEventsByPagination(
          @PathVariable int offset, @PathVariable int pageSize) {
    return userService.getAllEventsByPagination(offset,pageSize);
  }

  @Operation(summary = "Delete user")
  @DeleteMapping("/{email}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("email") String email) {
    userService.deleteUser(email);
  }

  @Operation(summary = "Get all Users by Interests")
  @GetMapping("/interests")
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> getAllEventsByInterest(@RequestBody InterestsIdsRequest interestsIds) {
    return userService.getAllUsersByInterest(interestsIds);
  }
  @Operation(summary = "Get all Users by skills")
  @GetMapping("/interests/skills")
  @ResponseStatus(HttpStatus.OK)
  public List<UserResponseDto> getAllEventsBySkills(@RequestBody InterestsIdsRequest interestsIds) {
    return userService.getAllUsersBySkills(interestsIds);
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
  @Operation(summary = "Upload new image in file system and attach to user")
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(value = "/{userId}/picture")
  public ImageResponseDto uploadImage(
          @PathVariable("userId") @Valid UUID userId,
          @RequestParam("image") @Valid MultipartFile image)
          throws IOException {
    return imageService.uploadImage(image, userId,null);
  }

  @Operation(summary = "Get image of user")
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/{userId}/picture", produces = "image/png")
  public byte[] getImage(@PathVariable("userId") @Valid UUID userId) throws IOException {
    return imageService.downloadImageForUser(userId);
  }

  @Operation(summary = "Delete image from file system and detach from user")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/{userId}/picture")
  public void deleteImage(@PathVariable("userId") @Valid UUID userId) throws IOException {
    imageService.deleteImageForUser(userId);
  }
}
