package com.idolu.product.presentation.product.request;

import com.idolu.product.application.command.ProductCreateCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductCreateRequest {


    @Min(value = 1, message = "회원사 id는 1보다 커야 합니다.")
    private Long storeId;

    @Min(value = 1, message = "재고는 1보다 커야 합니다.")
    private Integer stock;

    @NotBlank(message = "상품 이름을 필수입니다.")
    private String name;

    @NotBlank(message = "상품 상태값은 필수입니다.")
    private String status;

    private Boolean applyRoundDiscount;

    @Min(value = 1_000, message = "가격은 1000원보다 커야 합니다.")
    private BigDecimal basicPrice;

    @Min(value = 1_000, message = "가격은 1000원보다 커야 합니다.")
    private BigDecimal sellingPrice;

    @NotNull(message = "할인율 입력은 필수입니다.")
    private Integer discountRate;

    @NotNull(message = "기간별 할인 여부 입력은 필수입니다.")
    private Boolean discountOneTime;

    @Min(value = 1, message = "계약 기간은 1보다 커야 합니다.")
    private Integer contractPeriod;

    @NotBlank(message = "계약 기간 단위는 필수입니다.")
    private String contractPeriodUnitCode;

    @Min(value = 1, message = "서비스 제공주기는 1보다 커야 합니다.")
    private Integer servicePeriod;

    @NotBlank(message = "서비스 제공주기 단위는 필수입니다.")
    private String servicePeriodUnitCode;

    @NotBlank(message = "이미지 url은 필수입니다.")
    private String imageUrl;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @Size(min = 1, message = "상품은 최소 하나의 카테고리에 속해야 합니다.")
    private List<String> categories;

    private List<ProductDiscountDto> productDiscounts;

    private List<ProductDescriptionDto> productDescriptions;

    private List<ProductImageDto> productImages;

    public ProductCreateCommand toCommand() {
        return ProductCreateCommand.builder()
                .name(this.name)
                .stock(this.stock)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .price(this.basicPrice)
                .status(this.status)
                .categories(categories)
                .build();
    }
}
