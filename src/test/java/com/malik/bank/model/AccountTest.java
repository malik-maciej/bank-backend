package com.malik.bank.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

    private Account account;

    @BeforeEach
    void initializeAccount() {
        account = new Account();
    }

    @Test
    void shouldAddMoney() {
        // when
        account.addMoney(BigDecimal.valueOf(150));

        // then
        assertEquals(150, account.getBalance().intValue());
        assertTrue(account.toString().contains("balance=150"));
    }

    @Test
    void shouldSubtractMoney() {
        // given
        account.addMoney(BigDecimal.valueOf(250));

        // when
        account.subtractMoney(BigDecimal.valueOf(200));

        // then
        assertEquals(50, account.getBalance().intValue());
    }
}