package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.exception.UserNotFoundException;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.UserRepository;
import com.hackathon.netplatform.security.jwt.JwtUtils;
import com.hackathon.netplatform.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final JwtUtils jwtUtils;
  private final ModelMapper modelMapper;

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
}
