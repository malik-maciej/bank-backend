package com.malik.bank.service;

import com.malik.bank.UserRole;
import com.malik.bank.model.Contact;
import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserDetailsServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsService userDetailsService = new UserDetailsServiceImpl(userRepository);
    private final String username = "username";

    @Test
    void shouldLoadUserByUsername() {
        // given
        User newUser = new User();
        newUser.setUsername("admin");
        newUser.setPassword("password");
        newUser.setName("Name");
        newUser.setSurname("Surname");
        newUser.setIdNumber("ABC123456");
        newUser.setRole(UserRole.ADMIN.toString());
        newUser.setContact(new Contact());

        given(userRepository.findByUsername(username)).willReturn(Optional.of(newUser));

        // when + then
        assertEquals(newUser, userDetailsService.loadUserByUsername(username));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotInDb() {
        // given
        given(userRepository.findByUsername(username)).willReturn(empty());

        // when + then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userDetailsService.loadUserByUsername(username));

        assertEquals("Invalid username", exception.getMessage());
    }
}