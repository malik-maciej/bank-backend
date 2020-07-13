package com.malik.bank.controller;

import com.malik.bank.model.Account;
import com.malik.bank.model.Contact;
import com.malik.bank.model.User;
import com.malik.bank.repository.AccountRepository;
import com.malik.bank.repository.UserRepository;
import com.malik.bank.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@RestController
@IllegalExceptionControllerProcessing
@RequestMapping("/api/user")
class UserController {

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Could not find user - id: ";

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    UserController(UserRepository userRepository, AccountRepository accountRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/dashboard")
    String getDashboard() {
        return "dashboard";
    }

    @GetMapping()
    ResponseEntity<User> getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map((ResponseEntity::ok))
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @GetMapping("/{id}")
    ResponseEntity<User> getUser(@PathVariable long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @GetMapping("/{id}/contact")
    ResponseEntity<Contact> getContactByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getContact()))
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @GetMapping("/{id}/accounts")
    ResponseEntity<Set<Account>> getAccountsByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getAccounts()))
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @GetMapping("/{id}/active-accounts")
    ResponseEntity<Set<Account>> getActiveAccountsByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> accountRepository.findAllByOwnerIdAndActiveIsTrue(user.getId()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PostMapping("/{id}/create-account")
    ResponseEntity<?> createAccount(@PathVariable long id, @RequestBody @Valid Account account, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalStateException(getErrorMessage(result));
        }

        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id);
        }

        if (accountRepository.countAccountsByOwnerIdAndActiveIsTrue(user.get().getId()) > 1) {
            throw new IllegalStateException("This user cannot have more active accounts");
        }

        account.setNumber(accountService.generateAccountNumber());
        if (accountRepository.existsByNumber(account.getNumber())) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Such account number already exists");
        }

        account.setOwner(user.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(accountRepository.save(account));
    }

    static String getErrorMessage(BindingResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("Fields: ");
        result.getFieldErrors().forEach(error ->
                sb.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(", ")
        );
        sb.append("complete all properly.");
        return sb.toString();
    }
}