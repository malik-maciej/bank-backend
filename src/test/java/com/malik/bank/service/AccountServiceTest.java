package com.malik.bank.service;

import com.malik.bank.AccountType;
import com.malik.bank.model.Account;
import com.malik.bank.model.User;
import com.malik.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldGetGeneratedAccountNumber() {
        // when
        String accountNumber = accountService.generateAccountNumber();

        // then
        assertEquals(accountNumber.length(), 26);
        assertEquals(accountNumber.substring(0, 10), SPECIAL_BANK_NUMBER);
    }

    @Test
    void shouldGetUpdateAccountName() {
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
        accountService.createAccount(account, new User(), "02105000011845130074030191");

        // then
        then(accountRepository).should().save(account);
        assertAll(
                () -> assertNotNull(account.getOwner()),
                () -> assertEquals(account.getNumber(), "02105000011845130074030191"),
                () -> assertEquals(account.getName(), "Normal account"),
                () -> assertEquals(account.getType(), AccountType.CHECKING.toString()),
                () -> assertTrue(account.isActive()),
                () -> assertEquals(account.getBalance(), BigDecimal.ZERO)
        );
    }
}