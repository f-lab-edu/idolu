package com.idolu.product.domain.product;


import com.idolu.product.application.command.ProductUpdateCommand;
import com.idolu.product.domain.category.Category;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@SuperBuilder
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

    private Boolean deleted;

    @Version
    private Integer version;

    @Transient
    private List<Category> categories;

    public Product withCategories(List<Category> categories) {
        this.categories = categories;
        return this;
    }

    public Product changeInfo(ProductUpdateCommand command) {
        return Product.builder()
                .productId(this.productId)
                .stock(command.getStock())
                .name(command.getName())
                .imageUrl(command.getImageUrl())
                .description(command.getDescription())
                .price(command.getPrice())
                .status(command.getStatus())
                .deleted(command.getDeleted())
                .version(this.version)
                .createdAt(this.getCreatedAt())
                .build();
    }
}
