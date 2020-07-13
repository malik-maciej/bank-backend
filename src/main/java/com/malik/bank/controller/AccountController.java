package com.malik.bank.controller;

import com.malik.bank.model.Account;
import com.malik.bank.repository.AccountRepository;
import com.malik.bank.service.AccountService;
import org.springframework.http.HttpStatus;
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

    private final AccountRepository repository;
    private final AccountService service;

    AccountController(AccountRepository repository, AccountService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/{id}")
    ResponseEntity<Account> getAccount(@PathVariable long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PutMapping("/{id}/disable")
    ResponseEntity<?> disableAccount(@PathVariable long id) {
        Optional<Account> account = repository.findById(id);
        if (!account.isPresent()) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id);
        }

        if (account.get().getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalStateException("This account cannot be deleted because it still has money");
        }

        account.get().setActive(false);
        repository.save(account.get());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-name")
    ResponseEntity<?> changeAccountName(@PathVariable long id, @RequestBody Account toUpdate) {
        return repository.findById(id)
                .map(account -> {
                    if (toUpdate.getName().length() < 5 || toUpdate.getName().length() > 30) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Name size must be between 5 and 30.");
                    }
                    service.updateAccountName(account, toUpdate.getName());
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }
}