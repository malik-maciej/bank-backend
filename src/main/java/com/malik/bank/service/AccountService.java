package com.malik.bank.service;

import com.malik.bank.model.Account;
import com.malik.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final String SPECIAL_BANK_NUMBER = "0210500001";

    private final AccountRepository repository;

    AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public String generateAccountNumber() {
        String numbers = "0123456789";
        StringBuilder builder = new StringBuilder();
        builder.append(SPECIAL_BANK_NUMBER);

        int count = 16;
        while (count-- != 0) {
            int character = (int) (Math.random() * numbers.length());
            builder.append(numbers.charAt(character));
        }

        return builder.toString();
    }

    public Account updateAccountName(Account account, String newName) {
        account.setName(newName);
        return repository.save(account);
    }
}