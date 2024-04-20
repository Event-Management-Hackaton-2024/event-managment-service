package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EditUserRequestDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.model.User;

import java.util.UUID;

public interface UserService {

  User getUserByEmail(String email);

  UserResponseDto getUserByToken(String token);

  void deleteUser(String email);

  UserResponseDto editUser(String email, EditUserRequestDto editUserRequestDto);

  UserResponseDto addFollower(UUID followerId);

  UserResponseDto unfollowUser(UUID followedUserId);

  User getUserById(UUID id);
}
