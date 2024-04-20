package com.hackathon.netplatform.service;

import com.hackathon.netplatform.dto.request.EditUserRequestDto;
import com.hackathon.netplatform.dto.request.InterestsIdsRequest;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.model.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface UserService {

  User getUserByEmail(String email);

  List<UserResponseDto> gerAllUsersResponse();

  Page<UserResponseDto> getAllEventsByPagination(int offset, int pageSize);

  UserResponseDto getUserResponse(String email);

  void deleteUser(String email);

  UserResponseDto editUser(String email, EditUserRequestDto editUserRequestDto);

  UserResponseDto addFollower(UUID followerId);

  UserResponseDto unfollowUser(UUID followedUserId);

  User getUserById(UUID id);

  List<UserResponseDto> getAllUsersByInterest(InterestsIdsRequest interestsIds);

  List<UserResponseDto> getAllUsersBySkills(InterestsIdsRequest interestsIds);
}
