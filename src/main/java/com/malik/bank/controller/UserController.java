package com.malik.bank.controller;

import com.malik.bank.model.Account;
import com.malik.bank.model.Contact;
import com.malik.bank.model.User;
import com.malik.bank.repository.AccountRepository;
import com.malik.bank.repository.UserRepository;
import com.malik.bank.service.AccountService;
import com.malik.bank.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@RestController
@IllegalExceptionControllerProcessing
@RequestMapping("/api/user")
class UserController {

    private static final String ILLEGAL_ARGUMENT_MESSAGE = "Could not find user - id: ";

    private final UserRepository userRepository;
    private final UserService userService;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    UserController(UserRepository userRepository, UserService userService,
                   AccountRepository accountRepository, AccountService accountService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @GetMapping("/dashboard")
    String getDashboard() {
        return "dashboard";
    }

    @GetMapping
    ResponseEntity<User> getUser(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .map((ResponseEntity::ok))
                .orElse(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @GetMapping("/{id}")
    ResponseEntity<User> getUser(@PathVariable long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @GetMapping("/{id}/contact")
    ResponseEntity<Contact> getContactByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getContact()))
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @GetMapping("/{id}/accounts")
    ResponseEntity<Set<Account>> getAccountsByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(user.getAccounts()))
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @GetMapping("/{id}/active-accounts")
    ResponseEntity<Set<Account>> getActiveAccountsByUserId(@PathVariable long id) {
        return userRepository.findById(id)
                .map(user -> accountRepository.findAllByOwnerIdAndActiveIsTrue(user.getId()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
    }

    @PatchMapping("/change-password")
    ResponseEntity<?> changePassword(@RequestParam("password") String toUpdate, Principal principal) {
        String password = toUpdate.trim();
        if (password.length() < 8 || password.length() > 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password size must be between 8 and 20");
        }

        return userRepository.findByUsername(principal.getName())
                .map(user -> {
                    userService.changePassword(user, password);
                    return ResponseEntity.ok().build();
                })
                .orElseThrow(() -> new IllegalStateException("User error"));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER_ADVISOR')")
    @PostMapping("/{id}/create-account")
    ResponseEntity<?> createAccount(@PathVariable long id, @RequestBody @Valid Account account, BindingResult result) {
        if (result.hasErrors()) {
            throw new IllegalStateException(getErrorMessage(result));
        }

        String accountNumber = accountService.generateAccountNumber();
        if (accountRepository.existsByNumber(accountNumber)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Such account number already exists");
        }

        return userRepository.findById(id)
                .map(user -> {
                    if (accountRepository.countAccountsByOwnerIdAndActiveIsTrue(user.getId()) > 1) {
                        throw new IllegalStateException("This user cannot have more active accounts");
                    }
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(accountService.createAccount(account, user, accountNumber));
                })
                .orElseThrow(() -> new IllegalArgumentException(ILLEGAL_ARGUMENT_MESSAGE + id));
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