package com.malik.bank.service;

import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void shouldChangePassword() {
        // given
        User user = new User();
        user.setName("David");
        user.setSurname("Smith");
        user.setPassword(passwordEncoder.encode("password"));

        // when
        userService.changePassword(user, "newpassword");

        // then
        then(userRepository).should().save(user);
        assertEquals(user.getPassword(), passwordEncoder.encode("newpassword"));
        assertEquals(user.getSurname(), "Smith");
    }
}