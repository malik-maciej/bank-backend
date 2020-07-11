package com.malik.bank.controller;

import com.malik.bank.model.Account;
import com.malik.bank.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@IllegalExceptionControllerProcessing
@RequestMapping("/api/account")
class AccountController {

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Could not find account - id: ";

    private AccountRepository accountRepository;

    AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{id}")
    ResponseEntity<Account> getAccount(@PathVariable long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PutMapping("/{id}/disable")
    ResponseEntity<?> disableAccount(@PathVariable long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (!account.isPresent()) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id);
        }

        if (account.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("This account cannot be deleted because it still has money");
        }

        account.get().setActive(false);
        accountRepository.save(account.get());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-name")
    ResponseEntity<?> changeAccountName(@PathVariable long id, @RequestBody Account toUpdate) {
        return accountRepository.findById(id)
                .map(account -> {
                    account.setName(toUpdate.getName());
                    accountRepository.save(account);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }
}