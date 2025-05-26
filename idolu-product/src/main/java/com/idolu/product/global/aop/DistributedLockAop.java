package com.idolu.product.global.aop;

import com.idolu.product.global.annotation.DistributedLock;
import com.idolu.product.global.utils.RedissonKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private final RedissonReactiveClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(com.idolu.product.global.annotation.DistributedLock)")
    public Mono<Object> lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
        String baseKey = distributedLock.lockName();
        String dynamicKey = RedissonKeyGenerator.generateDynamicKey(
                distributedLock.identifier(),
                joinPoint.getArgs(),
                distributedLock.paramClassType(),
                signature.getParameterNames());
        long threadId = ThreadLocalRandom.current().nextLong();

        return redissonClient.getLock(baseKey + ":" + dynamicKey)
                .tryLock(
                        distributedLock.waitTime(),
                        distributedLock.leaseTime(),
                        distributedLock.timeUnit(),
                        threadId)
                .handle((lockAcquired, sink) -> {
                    log.info("락 획득: {}, threadId: {}", lockAcquired, threadId);
                    if (lockAcquired) sink.next(true);
                    else sink.error(new IllegalArgumentException("락을 획득하지 못했습니다."));
                })
                .flatMap(lockAcquired -> {
                    try {
                        return aopForTransaction.proceed(joinPoint)
                                .flatMap(result -> {
                                    log.info("락 해제 시도 threadId: {}", threadId);
                                    return redissonClient.getLock(baseKey + ":" + dynamicKey)
                                            .unlock(threadId)
                                            .thenReturn(result);
                                });
                    } catch (Throwable e) {
                        return Mono.error(e);
                    }
                });
    }
}
