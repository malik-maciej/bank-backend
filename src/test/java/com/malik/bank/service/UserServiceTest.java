package com.malik.bank.service;

import com.malik.bank.model.User;
import com.malik.bank.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class UserServiceTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);

    private final UserService service = new UserService(repository, encoder);

    @Test
    void shouldAddAdminToDb() {
        // when
        service.addAdminToDb();

        // then
        then(repository).should().save(any(User.class));
    }
}