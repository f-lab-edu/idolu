package com.idolu.product.presentation.product.response;

import com.idolu.product.domain.product.type.PeriodUnitCode;
import com.idolu.product.domain.product.type.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductItemDto {

    private Long productId; // 상품 id
    private String name; // 상품명
    private ProductStatus productStatus; // 상품 상태
    private BigDecimal basicPrice; // 정가
    private BigDecimal sellingPrice; // 판매가
    private Integer discountRate; // 할인율
    private Integer contractPeriod; // 계약 기간
    private PeriodUnitCode contractPeriodUnitCode; // 계약 기간 단위
    private Integer servicePeriod; // 서비스 제공주기
    private PeriodUnitCode servicePeriodUnitCode; // 서비스 제공주기 단위

    private String thumbnailUrl; // 썸네일 url
}
