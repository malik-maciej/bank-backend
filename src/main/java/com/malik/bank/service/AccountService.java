package com.malik.bank.service;

import com.malik.bank.model.Account;
import com.malik.bank.model.User;
import com.malik.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final String SPECIAL_BANK_NUMBER = "0210500001";

    private final AccountRepository accountRepository;

    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account updateAccountName(Account account, String newName) {
        account.setName(newName);
        return accountRepository.save(account);
    }

    public Account disableAccount(Account account) {
        account.setActive(false);
        return accountRepository.save(account);
    }

    public Account createAccount(Account account, User user) {
        account.setNumber(generateAccountNumber());
        account.setOwner(user);
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        String numbers = "0123456789";
        StringBuilder builder = new StringBuilder();
        builder.append(SPECIAL_BANK_NUMBER);

        int count = 16;
        while (count-- != 0) {
            int character = (int) (Math.random() * numbers.length());
            builder.append(numbers.charAt(character));
        }

        if (accountRepository.findByNumber(builder.toString()).isPresent()) {
            throw new RuntimeException("Generated account number exists");
        }

        return builder.toString();
    }
}