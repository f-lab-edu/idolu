package com.idolu.product.domain.store;

import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@EqualsAndHashCode(of = "storeId", callSuper = false)
public class Store extends BaseEntity {

    @Id
    private Long storeId;

    private String storeCode;

    private String storeName;

    private String representationName;

    private String baseAddress;

    private String detailAddress;

    private String profileImageUrl;

    private Boolean deleted;

    @Builder
    public Store(Long storeId, String storeCode, String storeName, String representationName, String baseAddress, String detailAddress, String profileImageUrl, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.storeId = storeId;
        this.storeCode = storeCode;
        this.storeName = storeName;
        this.representationName = representationName;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
        this.profileImageUrl = profileImageUrl;
        this.deleted = deleted;
    }
}
