package com.malik.bank.service;

import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void shouldAddAdminToDb() {
        // when
        userService.addAdminToDb();

        // then
        then(userRepository).should().save(any(User.class));
    }
}