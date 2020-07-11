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

    private final UserRepository repository = mock(UserRepository.class);
    private final UserDetailsService service = new UserDetailsServiceImpl(repository);
    private final String username = "username";

    @Test
    void shouldLoadUserByUsername() {
        // given
        User newUser = new User();
        newUser.setUsername("admin");
        newUser.setPassword("password");
        newUser.setName("Name");
        newUser.setSurname("Surname");
        newUser.setRole(UserRole.ADMIN.toString());
        newUser.setContact(new Contact());

        given(repository.findByUsername(username)).willReturn(Optional.of(newUser));

        // when
        UserDetails user = service.loadUserByUsername(username);

        // then
        assertEquals(newUser, user);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotInDb() {
        // given
        given(repository.findByUsername(username)).willReturn(empty());

        // when + then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.loadUserByUsername(username));

        assertEquals("Invalid username", exception.getMessage());
    }
}