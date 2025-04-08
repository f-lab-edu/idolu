package com.idolu.product.domain.product;

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
@EqualsAndHashCode(of = "productDescriptionId", callSuper = false)
public class ProductDescription extends BaseEntity {

    @Id
    private Long productDescriptionId;

    private Long productId; // 상품 id

    private String title; // 항목

    private String content; // 내용

    private Integer sortNumber; // 정렬 순서

    private Boolean deleted = false;
}
