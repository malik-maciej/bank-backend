package com.malik.bank.service;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final String SPECIAL_BANK_NUMBER = "0210500001";

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

}