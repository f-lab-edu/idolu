package com.idolu.product.application.product.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductDiscount;
import com.idolu.product.domain.product.ProductImage;
import com.idolu.product.domain.product.type.PeriodUnitCode;
import com.idolu.product.domain.product.type.ProductStatus;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.presentation.product.request.ProductDiscountCreateDto;
import com.idolu.product.presentation.product.request.ProductImageCreateDto;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.idolu.product.domain.product.type.PeriodUnitCode.toPeriodUnitCode;
import static com.idolu.product.domain.product.type.ProductStatus.validateInitialState;

@Getter
@Slf4j
public class ProductCreateCommand {

    private Long storeId;

    private Integer stock;

    private String name;

    private ProductStatus productStatus;

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

    private List<ProductImageCreateCommand> productImageCreateCommands;

    private List<ProductDiscountCreateCommand> productDiscountCreateCommands;

    private Map<String, String> productInformation;

    @Builder
    public ProductCreateCommand(Long storeId, Integer stock, String name, String status, Boolean applyRoundDiscount, BigDecimal basicPrice, BigDecimal sellingPrice, Integer discountRate, Boolean discountOneTime, Integer contractPeriod, String contractPeriodUnitCode, Integer servicePeriod, String servicePeriodUnitCode, List<String> categories, List<ProductImageCreateDto> productImageCreateDtos, List<ProductDiscountCreateDto> productDiscountCreateDtos, Map<String, String> productInformation) {
        this.storeId = storeId;
        this.stock = stock;
        this.name = name;
        this.productStatus = validateInitialState(status);
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
        this.deleted = false;
        this.productImageCreateCommands = productImageCreateDtos.stream()
                .map(dto -> ProductImageCreateCommand.builder()
                        .imageType(dto.getImageType())
                        .url(dto.getUrl())
                        .sortNumber(dto.getSortNumber())
                        .build())
                .toList();
        this.productDiscountCreateCommands = productDiscountCreateDtos.stream()
                .map(dto -> ProductDiscountCreateCommand.builder()
                        .discountRound(dto.getDiscountRound())
                        .discountValue(dto.getDiscountValue())
                        .discountCode(dto.getDiscountCode())
                        .build())
                .toList();
        this.productInformation = productInformation;
    }

    public Product toEntity(List<Category> categories) throws JsonProcessingException {

        return Product.builder()
                .storeId(this.storeId)
                .productIdentifier(UUID.randomUUID().toString())
                .name(this.name)
                .stock(this.stock)
                .applyRoundDiscount(this.applyRoundDiscount)
                .basicPrice(this.basicPrice)
                .sellingPrice(this.sellingPrice)
                .discountRate(this.discountRate)
                .discountOneTime(this.discountOneTime)
                .productStatus(this.productStatus)
                .contractPeriod(this.contractPeriod)
                .contractPeriodUnitCode(this.contractPeriodUnitCode)
                .servicePeriod(this.servicePeriod)
                .servicePeriodUnitCode(this.servicePeriodUnitCode)
                .deleted(this.deleted)
                .productCategories(categories.stream()
                        .map(category -> ProductCategory.builder()
                                .categoryId(category.getCategoryId())
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .productImages(productImageCreateCommands.stream()
                        .map(productImageCreateCommand -> ProductImage.builder()
                                .imageType(productImageCreateCommand.getImageType())
                                .url(productImageCreateCommand.getUrl())
                                .sortNumber(productImageCreateCommand.getSortNumber())
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .productDiscounts(productDiscountCreateCommands.stream()
                        .map(productDiscountCreateCommand -> ProductDiscount.builder()
                                .discountRound(productDiscountCreateCommand.getDiscountRound())
                                .discountCode(productDiscountCreateCommand.getDiscountCode())
                                .discountValue(productDiscountCreateCommand.getDiscountValue())
                                .build())
                        .collect(Collectors.toUnmodifiableList()))
                .productInformation(new ObjectMapper().writeValueAsString(this.productInformation.toString()))
                .build();
    }
}
