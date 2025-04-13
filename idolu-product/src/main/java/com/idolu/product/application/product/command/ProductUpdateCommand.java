package com.idolu.product.application.product.command;

import com.idolu.product.domain.product.type.PeriodUnitCode;
import com.idolu.product.domain.product.type.ProductStatus;
import com.idolu.product.presentation.product.request.ProductDiscountCreateDto;
import com.idolu.product.presentation.product.request.ProductDiscountUpdateDto;
import com.idolu.product.presentation.product.request.ProductImageCreateDto;
import com.idolu.product.presentation.product.request.ProductImageUpdateDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.idolu.product.domain.product.type.PeriodUnitCode.*;
import static com.idolu.product.domain.product.type.ProductStatus.toProductStatus;

@Getter
public class ProductUpdateCommand {

    private Long productId;

    private Long storeId;

    private Integer stock;

    private String name;

    private ProductStatus status;

    private Boolean applyRoundDiscount;

    private BigDecimal basicPrice;

    private BigDecimal sellingPrice;

    private Integer discountRate;

    private Boolean discountOneTime;

    private Integer contractPeriod;

    private PeriodUnitCode contractPeriodUnitCode;

    private Integer servicePeriod;

    private PeriodUnitCode servicePeriodUnitCode;

    private List<String> categories;

    private Boolean deleted;

    private LocalDateTime updatedAt;

    private List<ProductDiscountUpdateCommand> productDiscounts;

    private List<ProductImageUpdateCommand> productImages;

    private Map<String, String> productInformation;

    @Builder
    public ProductUpdateCommand(String status, Long productId, Long storeId, Integer stock, String name, Boolean applyRoundDiscount, BigDecimal basicPrice, BigDecimal sellingPrice, Integer discountRate, Boolean discountOneTime, Integer contractPeriod, String contractPeriodUnitCode, Integer servicePeriod, String servicePeriodUnitCode, List<String> categories, Boolean deleted, LocalDateTime updatedAt, List<ProductDiscountUpdateDto> productDiscountDtos, List<ProductImageUpdateDto> productImageDtos, Map<String, String> productInformation) {
        this.productId = productId;
        this.status = toProductStatus(status);
        this.storeId = storeId;
        this.stock = stock;
        this.name = name;
        this.applyRoundDiscount = applyRoundDiscount;
        this.basicPrice = basicPrice;
        this.sellingPrice = sellingPrice;
        this.discountRate = discountRate;
        this.discountOneTime = discountOneTime;
        this.contractPeriod = contractPeriod;
        this.contractPeriodUnitCode = toPeriodUnitCode(contractPeriodUnitCode);
        this.servicePeriod = servicePeriod;
        this.servicePeriodUnitCode = toPeriodUnitCode(servicePeriodUnitCode);
        this.categories = categories;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.productDiscounts = productDiscountDtos.stream()
                .map(dto -> ProductDiscountUpdateCommand.builder()
                        .discountRound(dto.getDiscountRound())
                        .discountValue(dto.getDiscountValue())
                        .discountCode(dto.getDiscountCode())
                        .build())
                .toList();
        this.productImages = productImageDtos.stream()
                .map(dto -> ProductImageUpdateCommand.builder()
                        .imageType(dto.getImageType())
                        .url(dto.getUrl())
                        .sortNumber(dto.getSortNumber())
                        .build())
                .toList();
        this.productInformation = productInformation;
    }
}
