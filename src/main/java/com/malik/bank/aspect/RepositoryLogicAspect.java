package com.malik.bank.aspect;

import com.malik.bank.repository.AccountRepository;
import com.malik.bank.repository.ContactRepository;
import com.malik.bank.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
class RepositoryLogicAspect {

    private static final Logger ACCOUNT_LOGGER = LoggerFactory.getLogger(AccountRepository.class);
    private static final Logger CONTACT_LOGGER = LoggerFactory.getLogger(ContactRepository.class);
    private static final Logger USER_LOGGER = LoggerFactory.getLogger(UserRepository.class);

    @Pointcut("execution(* com.malik.bank.repository.AccountRepository.save(..))")
    static void accountRepositorySave() {
    }

    @Pointcut("execution(* com.malik.bank.repository.ContactRepository.save(..))")
    static void contactRepositorySave() {
    }

    @Pointcut("execution(* com.malik.bank.repository.UserRepository.save(..))")
    static void userRepositorySave() {
    }

    @After("accountRepositorySave()")
    void accountLogMethodCall(JoinPoint jp) {
        ACCOUNT_LOGGER.info("After {} with {}", jp.getSignature().getName(), jp.getArgs());
    }

    @After("contactRepositorySave()")
    void contactLogMethodCall(JoinPoint jp) {
        CONTACT_LOGGER.info("After {} with {}", jp.getSignature().getName(), jp.getArgs());
    }

    @After("userRepositorySave()")
    void userLogMethodCall(JoinPoint jp) {
        USER_LOGGER.info("After {} with {}", jp.getSignature().getName(), jp.getArgs());
    }
}