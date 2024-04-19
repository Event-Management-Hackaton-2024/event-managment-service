package com.hackathon.netplatform.service.impl;

import com.hackathon.netplatform.dto.request.AuthRequestDto;
import com.hackathon.netplatform.dto.request.UserRequestDto;
import com.hackathon.netplatform.dto.response.AuthResponseDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.exception.IncorrectPasswordException;
import com.hackathon.netplatform.exception.UserAlreadyExistsException;
import com.hackathon.netplatform.model.Role;
import com.hackathon.netplatform.model.RoleName;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.UserRepository;
import com.hackathon.netplatform.security.jwt.JwtUtils;
import com.hackathon.netplatform.service.AuthService;
import com.hackathon.netplatform.service.RoleService;
import com.hackathon.netplatform.service.UserService;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final ModelMapper modelMapper;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final RoleService roleService;
  private final UserService userService;

  @Override
  public AuthResponseDto authenticate(AuthRequestDto authRequestDto) {
    userService.getUserByEmail(authRequestDto.getEmail());

    Authentication authentication = getAuthentication(authRequestDto);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtUtils.generateJwtTokenForUser(authentication);

    AuthResponseDto authResponseDto = new AuthResponseDto();
    authResponseDto.setToken(token);
    return authResponseDto;
  }

  @Override
  @Transactional
  public UserResponseDto registerUser(UserRequestDto userRequestDto) {
    checkForUsernameInDatabase(userRequestDto);
    checkForEmailInDatabase(userRequestDto);

    User user = modelMapper.map(userRequestDto, User.class);

    setPasswordToUser(userRequestDto, user);
    setRoleToUser(user);
    userRepository.save(user);

    return modelMapper.map(user, UserResponseDto.class);
  }

  private Authentication getAuthentication(AuthRequestDto authRequestDto) {
    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            authRequestDto.getEmail(), authRequestDto.getPassword()));
  }

  private void setRoleToUser(User user) {
    roleService.createRolesInDatabase();

    Role role = roleService.getRole(RoleName.USER.name());

    if (userRepository.count() == 0) {
      role = roleService.getRole(RoleName.ADMIN.name());
    }

    user.setRoles(Set.of(role));
  }

  private void setPasswordToUser(UserRequestDto userRequestDto, User user) {
    String encodedPassword = bCryptPasswordEncoder.encode(userRequestDto.getPassword());

    if (!bCryptPasswordEncoder.matches(userRequestDto.getConfirmPassword(), encodedPassword)) {
      throw new IncorrectPasswordException();
    }

    user.setPassword(encodedPassword);
  }

  private void checkForEmailInDatabase(UserRequestDto userRequestDto) {
    if (userRepository.existsByEmail(userRequestDto.getEmail())) {
      throw new UserAlreadyExistsException(userRequestDto.getEmail());
    }
  }

  private void checkForUsernameInDatabase(UserRequestDto userRequestDto) {
    if (userRepository.existsByUsername(userRequestDto.getUsername())) {
      throw new UserAlreadyExistsException(userRequestDto.getUsername());
    }
  }
}
