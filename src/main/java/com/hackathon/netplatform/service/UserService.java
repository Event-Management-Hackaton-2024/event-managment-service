package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.model.User;

public interface UserService {

  User getUserByEmail(String email);

  UserResponseDto getUserByToken(String token);

  void deleteUser(String email);
}
