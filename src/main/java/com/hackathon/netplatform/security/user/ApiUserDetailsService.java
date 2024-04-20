package com.hackathon.netplatform.security.user;

import com.hackathon.netplatform.exception.UserNotFoundException;
import com.hackathon.netplatform.model.User;
import com.hackathon.netplatform.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApiUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

    return ApiUserDetails.buildUserDetails(user);
  }
}
