package com.idolu.product.infrastructure.out.persistence.adapter;

import com.idolu.product.domain.store.Store;
import com.idolu.product.global.exception.ErrorCode;
import com.idolu.product.global.exception.StoreNotFoundException;
import com.idolu.product.infrastructure.out.persistence.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StoreAdapter {

    private final StoreRepository storeRepository;

    @Transactional(readOnly = true)
    public Mono<Store> findByStoreCode(String storeCode) {
        return storeRepository.findByStoreCode(storeCode)
                .switchIfEmpty(Mono.error(new StoreNotFoundException(ErrorCode.STORE_NOT_FOUND, storeCode)));
    }
}
