package com.malik.bank.service;

import com.malik.bank.model.Address;
import com.malik.bank.model.Contact;
import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserService userService = new UserService(userRepository, passwordEncoder);

    private User user;

    @BeforeEach
    void initializeUser() {
        user = new User();
        user.setName("David");
        user.setSurname("Smith");
    }

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
        user.setPassword(passwordEncoder.encode("password"));

        // when
        userService.changePassword(user, "newpassword");

        // then
        then(userRepository).should().save(user);
        assertEquals(user.getPassword(), passwordEncoder.encode("newpassword"));
    }

    @Test
    void shouldAddUser() {
        // given
        Address address = new Address();
        Contact contact = new Contact();
        contact.setAddress(address);

        user.setContact(contact);
        user.setPassword("password");

        // when
        userService.addUser(user);

        // then
        then(userRepository).should().findByUsername(anyString());
        then(userRepository).should().save(user);
        assertAll(
                () -> assertEquals(user.getSurname(), "Smith"),
                () -> assertEquals(user.getPassword(), passwordEncoder.encode("password")),
                () -> assertEquals(user.getContact().getUser(), user),
                () -> assertEquals(user.getUsername().length(), 12)
        );
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUsernameExists() {
        // given
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(new User()));

        // when + then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userService.addUser(user));

        assertEquals("Generated username exists", exception.getMessage());
    }
}