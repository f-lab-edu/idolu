package com.idolu.product.domain.product;


import com.idolu.product.domain.category.Category;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@ToString
@Table("product")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "productId", callSuper = false)
public class Product extends BaseEntity {

    @Id
    private Long productId;

    private Integer stock;

    private String name;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private ProductStatus status;

    private Boolean isDeleted;

    @Version
    private Integer version;

    @Transient
    private List<Category> categories;
}
