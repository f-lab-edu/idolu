package com.idolu.product.domain.productcategory;

import com.idolu.product.domain.product.Product;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Table("product_category")
@EqualsAndHashCode(of = "productCategoryId", callSuper = false)
public class ProductCategory extends BaseEntity {

    @Id
    private Long productCategoryId;

    private Long productId;

    private Long categoryId;

    private Boolean deleted;

    @Builder
    public ProductCategory(LocalDateTime createdAt, LocalDateTime updatedAt, Long productCategoryId, Long productId, Long categoryId, Boolean deleted) {
        super(createdAt, updatedAt);
        this.productCategoryId = productCategoryId;
        this.productId = productId;
        this.categoryId = categoryId;
        this.deleted = deleted;
    }

    public ProductCategory withProductId(Long productId) {
        this.productId = productId;
        return this;
    }

    public static List<ProductCategory> from(Product product) {
        return product.getProductCategories().stream()
                .map(category -> ProductCategory.builder()
                        .productId(product.getProductId())
                        .categoryId(category.getCategoryId())
                        .deleted(false)
                        .build())
                .collect(Collectors.toList());
    }
}
