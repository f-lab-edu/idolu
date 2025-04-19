package com.idolu.product.domain.product;


import com.idolu.product.application.product.command.ProductUpdateCommand;
import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.type.PeriodUnitCode;
import com.idolu.product.domain.product.type.ProductStatus;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
@Table("product")
@EqualsAndHashCode(of = "productId", callSuper = false)
public class Product extends BaseEntity {

    @Id
    private Long productId; // 상품 id

    private String productIdentifier; // 상품 외부 id

    private Long storeId; // 회원사 id

    private Integer stock; // 재고

    private String name; // 상품명

    private ProductStatus productStatus; // 상품 상태

    private Boolean applyRoundDiscount; // 기간별 할인 여뷰

    private BigDecimal basicPrice; // 기본 가격

    private BigDecimal sellingPrice; // 할인 가격

    private Integer discountRate; // 할인율

    private Boolean discountOneTime; // 일회성 할인 여부

    private Integer contractPeriod; // 계약 기간

    private PeriodUnitCode contractPeriodUnitCode; // 계약 기간 단위

    private Integer servicePeriod; // 서비스 제공주기

    private PeriodUnitCode servicePeriodUnitCode; // 서비스 제공주기 단위

    private Map<String, String> productInformation; // 상품 정보

    private Boolean deleted = false; // 삭제 여부

    @Version
    private Integer version;

    @Transient
    private List<ProductCategory> productCategories;

    @Transient
    private List<ProductDiscount> productDiscounts;

    @Transient
    private List<ProductImage> productImages;

    @PersistenceCreator
    public Product(Long productId, String productIdentifier, Long storeId, Integer stock, String name, ProductStatus productStatus, Boolean applyRoundDiscount, BigDecimal basicPrice, BigDecimal sellingPrice, Integer discountRate, Boolean discountOneTime, Integer contractPeriod, PeriodUnitCode contractPeriodUnitCode, Integer servicePeriod, PeriodUnitCode servicePeriodUnitCode, Map<String, String> productInformation, Boolean deleted, Integer version, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this(productId, productIdentifier, storeId, stock, name,  productStatus, applyRoundDiscount, basicPrice, sellingPrice, discountRate, discountOneTime, contractPeriod, contractPeriodUnitCode, servicePeriod, servicePeriodUnitCode, productInformation, deleted, version, null, null, null, createdAt, updatedAt);
    }

    @Builder
    public Product(Long productId, String productIdentifier, Long storeId, Integer stock, String name, ProductStatus productStatus, Boolean applyRoundDiscount, BigDecimal basicPrice, BigDecimal sellingPrice, Integer discountRate, Boolean discountOneTime, Integer contractPeriod, PeriodUnitCode contractPeriodUnitCode, Integer servicePeriod, PeriodUnitCode servicePeriodUnitCode, Map<String, String> productInformation, Boolean deleted, Integer version, List<ProductCategory> productCategories, List<ProductDiscount> productDiscounts, List<ProductImage> productImages, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.productId = productId;
        this.productIdentifier = productIdentifier;
        this.storeId = storeId;
        this.stock = stock;
        this.name = name;
        this.productStatus = productStatus;
        this.applyRoundDiscount = applyRoundDiscount;
        this.basicPrice = basicPrice;
        this.sellingPrice = sellingPrice;
        this.discountRate = discountRate;
        this.discountOneTime = discountOneTime;
        this.contractPeriod = contractPeriod;
        this.contractPeriodUnitCode = contractPeriodUnitCode;
        this.servicePeriod = servicePeriod;
        this.servicePeriodUnitCode = servicePeriodUnitCode;
        this.productInformation = productInformation;
        this.deleted = deleted;
        this.version = version;
        this.productCategories = productCategories;
        this.productDiscounts = productDiscounts;
        this.productImages = productImages;
    }

    public Product withProductCategories(List<Category> categories) {
        this.productCategories = categories.stream()
                .map(category -> ProductCategory.builder()
                        .productId(this.productId)
                        .categoryId(category.getCategoryId())
                        .build())
                .collect(Collectors.toList());
        return this;
    }

    public Product changeInfo(ProductUpdateCommand command) {
        return Product.builder()
                .productId(this.productId)
                .storeId(this.storeId)
                .productIdentifier(this.productIdentifier)
                .stock(command.getStock())
                .name(command.getName())
                .productStatus(command.getStatus())
                .applyRoundDiscount(command.getApplyRoundDiscount())
                .basicPrice(command.getBasicPrice())
                .sellingPrice(command.getSellingPrice())
                .discountRate(command.getDiscountRate())
                .discountOneTime(command.getDiscountOneTime())
                .contractPeriod(command.getContractPeriod())
                .contractPeriodUnitCode(command.getContractPeriodUnitCode())
                .servicePeriod(command.getServicePeriod())
                .servicePeriodUnitCode(command.getServicePeriodUnitCode())
                .deleted(command.getDeleted())
                .version(this.version)
                .createdAt(this.getCreatedAt())
                .productDiscounts(command.getProductDiscounts().stream()
                        .map(discountCommand -> ProductDiscount.builder()
                                .productId(productId)
                                .discountRound(discountCommand.getDiscountRound())
                                .discountValue(discountCommand.getDiscountValue())
                                .discountCode(discountCommand.getDiscountCode())
                                .build())
                        .toList())
                .productImages(command.getProductImages().stream()
                        .map(imageCommand -> ProductImage.builder()
                                .productId(this.productId)
                                .imageType(imageCommand.getImageType())
                                .url(imageCommand.getUrl())
                                .sortNumber(imageCommand.getSortNumber())
                                .build())
                        .toList())
                .productInformation(command.getProductInformation())
                .build();
    }
}
