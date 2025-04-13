package com.idolu.product.domain.product;


import com.idolu.product.application.product.command.ProductUpdateCommand;
import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.type.PeriodUnitCode;
import com.idolu.product.domain.product.type.ProductStatus;
import com.idolu.product.domain.productcategory.ProductCategory;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@ToString
@SuperBuilder
@Table("product")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Builder.Default
    private Boolean deleted = false; // 삭제 여부

    @Version
    private Integer version;

    @Transient
    private List<ProductCategory> productCategories;

    @Transient
    private List<ProductDiscount> productDiscounts;

    @Transient
    private List<ProductImage> productImages;

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
                .stock(command.getStock())
                .name(command.getName())
                .basicPrice(command.getPrice())
                .productStatus(command.getStatus())
                .deleted(command.getDeleted())
                .version(this.version)
                .createdAt(this.getCreatedAt())
                .build();
    }
}
