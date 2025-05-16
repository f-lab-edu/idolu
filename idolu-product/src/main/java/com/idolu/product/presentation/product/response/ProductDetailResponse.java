package com.idolu.product.presentation.product.response;

import com.idolu.product.domain.product.type.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ProductDetailResponse {

    private Long productId;

    private String productIdentifier;

    private String name;

    private ProductStatus productStatus;

    private Boolean applyRoundDiscount;

    private PriceInformationDto price;

    private Boolean discountOneTime;

    private SubscriptionTermDto term;

    private Map<String, String> productInformation;

    private List<ProductImageDto> images;

    private List<ProductImageDto> detailImages;

    private List<DiscountRoundDto> discountRounds;
}
