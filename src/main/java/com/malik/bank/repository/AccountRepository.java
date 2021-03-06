package com.malik.bank.repository;

import com.malik.bank.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findByNumber(String number);

    int countAccountsByOwnerIdAndActiveIsTrue(long ownerId);

    Set<Account> findAllByOwnerIdAndActiveIsTrue(long ownerId);
}