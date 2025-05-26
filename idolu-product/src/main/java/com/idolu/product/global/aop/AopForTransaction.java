package com.idolu.product.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AopForTransaction {

    @Transactional
    public Mono<Object> proceed(ProceedingJoinPoint joinPoint) throws Throwable {
        return (Mono<Object>) joinPoint.proceed();
    }
}
