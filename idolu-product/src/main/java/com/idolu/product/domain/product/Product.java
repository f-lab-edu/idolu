package com.idolu.product.domain.product;


import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Getter
@Table("product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Product extends BaseEntity {

    @Id
    private Long id;

    private Integer stock;

    private String name;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private ProductStatus status;

    private Boolean isDeleted;

    @Version
    private Integer version;

    @Builder
    public Product(Integer stock, String name, String imageUrl, String description, BigDecimal price, ProductStatus status) {
        this.stock = stock;
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.status = status;
        this.isDeleted = false;
    }
}
