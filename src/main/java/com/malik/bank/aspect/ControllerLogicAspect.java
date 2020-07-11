package com.malik.bank.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
class ControllerLogicAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerLogicAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    static void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    static void putMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    static void patchMapping() {
    }

    @Before("postMapping()")
    void postMappingLogMethodCalls(JoinPoint jp) {
        LOGGER.info("Before {} with {}", jp.getSignature().getName(), jp.getArgs());
    }

    @Before("putMapping()")
    void putMappingLogMethodCalls(JoinPoint jp) {
        LOGGER.info("Before {} with {}", jp.getSignature().getName(), jp.getArgs());
    }

    @Before("patchMapping()")
    void patchMappingLogMethodCalls(JoinPoint jp) {
        LOGGER.info("Before {} with {}", jp.getSignature().getName(), jp.getArgs());
    }
}