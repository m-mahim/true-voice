package com.devrezaur.main.service;

import com.devrezaur.main.model.User;
import com.devrezaur.main.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void registerUser(String username, String password) throws Exception {
    User user = userRepository.findByUsername(username);
    if (user != null) {
      throw new Exception("User already exists with this username!");
    }

    user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setRole("ROLE_USER");
    userRepository.save(user);
  }

}
