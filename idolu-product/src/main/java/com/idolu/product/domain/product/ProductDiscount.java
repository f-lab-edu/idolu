package com.idolu.product.domain.product;


import com.idolu.product.domain.product.type.DiscountCode;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table
@Getter
@EqualsAndHashCode(of = "productDiscountId", callSuper = false)
public class ProductDiscount extends BaseEntity {

    @Id
    private Long productDiscountId; // 상품 할인 id

    private Long productId; // 상품 id

    private Integer discountRound; // 적용 회차

    private BigDecimal discountValue; // 할인 금액/할인율

    private DiscountCode discountCode; // 할인 유형

    private Boolean deleted;

    @Builder
    public ProductDiscount(Long productDiscountId, Long productId, Integer discountRound, BigDecimal discountValue, DiscountCode discountCode, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.productDiscountId = productDiscountId;
        this.productId = productId;
        this.discountRound = discountRound;
        this.discountValue = discountValue;
        this.discountCode = discountCode;
        this.deleted = deleted;
    }

    public ProductDiscount withProductId(Long productId) {
        this.productId = productId;
        return this;
    }
}
