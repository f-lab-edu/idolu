package com.idolu.product.domain.product;


import com.idolu.product.domain.product.type.DiscountCode;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "productDiscountId", callSuper = false)
public class ProductDiscount extends BaseEntity {

    @Id
    private Long productDiscountId; // 상품 할인 id

    private Long productId; // 상품 id

    private BigDecimal discountValue; // 할인 금액/할인율

    private DiscountCode discountCode; // 할인 유형

    private Boolean deleted = false;
}
