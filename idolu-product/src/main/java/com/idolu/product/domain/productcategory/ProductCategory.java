package com.idolu.product.domain.productcategory;

import com.idolu.product.domain.product.Product;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@Table("product_category")
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCategory extends BaseEntity {

    @Id
    @Column("product_category_id")
    private Long id;

    private Long productId;

    private Long categoryId;

    private Boolean isDeleted;

    public static List<ProductCategory> from(Product product) {
        return product.getCategories().stream()
                .map(category -> ProductCategory.builder()
                        .productId(product.getId())
                        .categoryId(category.getId())
                        .isDeleted(category.getIsDeleted() != null && category.getIsDeleted())
                        .build())
                .collect(Collectors.toList());
    }
}
