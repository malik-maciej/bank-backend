package com.malik.bank.model;

import com.malik.bank.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldGetAuthorities() {
        // given
        User user = new User();
        user.setRole(UserRole.ADMIN.toString());

        // when
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        // then
        assertAll(
                () -> assertNotNull(authorities),
                () -> assertEquals(authorities.size(), 1),
                () -> assertEquals(authorities.stream().findFirst().get().toString(), user.getRole())
        );
    }
}