package com.idolu.product.domain.store;

import com.idolu.product.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
