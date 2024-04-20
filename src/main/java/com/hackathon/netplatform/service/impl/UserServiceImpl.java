package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.request.EditUserRequestDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.exception.NoAuthenticatedUserException;
import com.hackathon.netplatform.exception.UserNotFoundException;
import com.hackathon.netplatform.exception.UserPermissionException;
import com.hackathon.netplatform.model.Interest;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.UserRepository;
import com.hackathon.netplatform.security.jwt.JwtUtils;
import com.hackathon.netplatform.service.InterestService;
import com.hackathon.netplatform.service.UserService;
import jakarta.transaction.Transactional;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JwtUtils jwtUtils;
  private final ModelMapper modelMapper;
  private final InterestService interestService;

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));
  }

  @Override
  public UserResponseDto getUserByToken(String token) {
    String email = jwtUtils.getUsernameFromToken(token);

    User user = getUserByEmail(email);
    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  @Transactional
  public void deleteUser(String email) {
    if (userRepository.existsByEmail(email)) {
      userRepository.deleteByEmail(email);
    } else {
      throw new UserNotFoundException(email);
    }
  }

  @Override
  public UserResponseDto editUser(String email, EditUserRequestDto editUserRequestDto) {
    User user = getUserByEmail(email);
    String currentUserEmail = getCurrentUser();

    if (!email.equals(currentUserEmail)) {
      throw new UserPermissionException();
    }

    setUserFields(editUserRequestDto, user);

    userRepository.save(user);

    return modelMapper.map(user, UserResponseDto.class);
  }

  @Override
  public UserResponseDto addFollower(UUID followerId) {
    User follower = getUserById(followerId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String currentUserEmail = authentication.getName();

    User currentUser = getUserByEmail(currentUserEmail);

    Set<User> followers = new HashSet<>(currentUser.getFollowers());
    followers.add(follower);
    currentUser.setFollowers(followers);
    userRepository.save(currentUser);

    return modelMapper.map(currentUser, UserResponseDto.class);
  }

  @Override
  public UserResponseDto unfollowUser(UUID followedUserId) {
    User follower = getUserById(followedUserId);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    String currentUserEmail = authentication.getName();

    User currentUser = getUserByEmail(currentUserEmail);

    Set<User> followers = new HashSet<>(currentUser.getFollowers());
    followers.remove(follower);
    currentUser.setFollowers(followers);
    userRepository.save(currentUser);

    return modelMapper.map(currentUser, UserResponseDto.class);
  }

  public User getUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  private void setUserFields(EditUserRequestDto editUserRequestDto, User user) {
    List<Interest> userInterests = getUserInterestsFromRequest(editUserRequestDto.getInterests());
    List<Interest> userSkills = getUserInterestsFromRequest(editUserRequestDto.getSkills());

    user.setInterests(userInterests);
    user.setSkills(userSkills);
    user.setFirstName(editUserRequestDto.getFirstName());
    user.setLastName(editUserRequestDto.getLastName());
    user.setUsername(editUserRequestDto.getUsername());
    user.setPhoneNumber(editUserRequestDto.getPhoneNumber());
  }

  private List<Interest> getUserInterestsFromRequest(List<String> interests) {
    List<Interest> userInterests = new ArrayList<>();
    for (String interest : interests) {
      Interest current = interestService.getInterest(interest);
      userInterests.add(current);
    }
    return userInterests;
  }

  private String getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null) {
      return authentication.getName();
    }
    throw new NoAuthenticatedUserException();
  }
}
