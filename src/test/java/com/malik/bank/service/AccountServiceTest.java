package com.malik.bank.service;

import com.malik.bank.model.Account;
import com.malik.bank.repository.AccountRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class AccountServiceTest {

    private static final String SPECIAL_BANK_NUMBER = "0210500001";

    private final AccountRepository repository = mock(AccountRepository.class);
    private final AccountService service = new AccountService(repository);

    @Test
    void shouldGetGeneratedAccountNumber() {
        // when
        String accountNumber = service.generateAccountNumber();

        // then
        assertEquals(accountNumber.length(), 26);
        assertEquals(accountNumber.substring(0, 10), SPECIAL_BANK_NUMBER);
    }

    @Test
    void shouldGetUpdateAccountName() {
        // given
        Account account = new Account();
        account.setName("Normal account");

        // when
        Account updateAccount = service.updateAccountName(account, "My account");

        // then
        assertEquals("My account", account.getName());
    }
}