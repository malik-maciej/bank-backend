package com.malik.bank.controller;

import com.malik.bank.UserRole;
import com.malik.bank.model.Account;
import com.malik.bank.repository.AccountRepository;
import com.malik.bank.repository.UserRepository;
import com.malik.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Set;

@RestController
@IllegalExceptionControllerProcessing
@RequestMapping("/api/account")
class AccountController {

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Could not find account - id: ";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    AccountController(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/all")
    ResponseEntity<Set<Account>> getActiveAccounts(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map(user -> accountRepository.findAllByOwnerIdAndActiveIsTrue(user.getId()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalStateException("User error"));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getAccount(@PathVariable long id, Principal principal) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));

        return userRepository.findByUsername(principal.getName())
                .map(loggedUser -> {
                    if (!account.getOwner().equals(loggedUser) && loggedUser.getRole().equals(UserRole.CUSTOMER))
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                    return ResponseEntity.ok(account);
                })
                .orElseThrow(() -> new IllegalStateException("User error"));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PutMapping("/{id}/disable")
    ResponseEntity<?> disableAccount(@PathVariable long id) {
        return accountRepository.findById(id)
                .map(account -> {
                    if (account.getBalance().compareTo(BigDecimal.ZERO) > 0)
                        throw new IllegalStateException("This account cannot be deleted because it still has money");
                    accountService.disableAccount(account);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PatchMapping("/{id}/change-name")
    ResponseEntity<?> changeAccountName(@PathVariable long id, @RequestBody Account toUpdate) {
        return accountRepository.findById(id)
                .map(account -> {
                    if (toUpdate.getName().length() < 5 || toUpdate.getName().length() > 30) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Name size must be between 5 and 30.");
                    }
                    accountService.updateAccountName(account, toUpdate.getName());
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }
}