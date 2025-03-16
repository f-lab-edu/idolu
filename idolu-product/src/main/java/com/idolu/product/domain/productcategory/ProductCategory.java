package com.idolu.product.domain.productcategory;

import com.idolu.product.domain.product.Product;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@Table("product_category")
@EqualsAndHashCode(of = "productCategoryId", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory extends BaseEntity {

    @Id
    private Long productCategoryId;

    private Long productId;

    private Long categoryId;

    private Boolean deleted;

    public static List<ProductCategory> from(Product product) {
        return product.getCategories().stream()
                .map(category -> ProductCategory.builder()
                        .productId(product.getProductId())
                        .categoryId(category.getCategoryId())
                        .deleted(category.getDeleted() != null && category.getDeleted())
                        .build())
                .collect(Collectors.toList());
    }
}
