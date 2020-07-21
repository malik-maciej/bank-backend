package com.malik.bank.service;

import com.malik.bank.AccountType;
import com.malik.bank.model.Account;
import com.malik.bank.model.User;
import com.malik.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class AccountServiceTest {

    private static final String SPECIAL_BANK_NUMBER = "0210500001";

    private final AccountRepository accountRepository = mock(AccountRepository.class);
    private final AccountService accountService = new AccountService(accountRepository);

    private Account account;

    @BeforeEach
    void initializeAccount() {
        account = new Account();
        account.setName("Normal account");
        account.setType(AccountType.CHECKING.toString());
        account.setActive(true);
    }

    @Test
    void shouldUpdateAccountName() {
        // when
        accountService.updateAccountName(account, "My account");

        // then
        then(accountRepository).should().save(account);
        assertEquals("My account", account.getName());
    }

    @Test
    void shouldDisableAccount() {
        // when
        accountService.disableAccount(account);

        // then
        then(accountRepository).should().save(account);
        assertFalse(account.isActive());
    }

    @Test
    void shouldCreateAccount() {
        // when
        accountService.createAccount(account, new User());

        // then
        then(accountRepository).should().findByNumber(anyString());
        then(accountRepository).should().save(account);
        assertAll(
                () -> assertNotNull(account.getOwner()),
                () -> assertNotNull(account.getNumber()),
                () -> assertEquals(account.getName(), "Normal account"),
                () -> assertEquals(account.getType(), AccountType.CHECKING.toString()),
                () -> assertEquals(account.getBalance(), BigDecimal.ZERO),
                () -> assertTrue(account.isActive())
        );
    }

    @Test
    void shouldGenerateProperlyAccountNumber() {
        // when
        accountService.createAccount(account, new User());

        // then
        assertEquals(account.getNumber().length(), 26);
        assertEquals(account.getNumber().substring(0, 10), SPECIAL_BANK_NUMBER);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUsernameExists() {
        // given
        given(accountRepository.findByNumber(anyString())).willReturn(Optional.of(new Account()));

        // when + then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> accountService.createAccount(account, new User()));

        assertEquals("Generated account number exists", exception.getMessage());
    }
}