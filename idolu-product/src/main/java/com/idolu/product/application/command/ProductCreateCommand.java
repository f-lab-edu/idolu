package com.idolu.product.application.command;

import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.type.ProductStatus;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

import static com.idolu.product.domain.product.type.ProductStatus.validateInitialState;

@Getter
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ProductCreateCommand {

    private String name;

    private Integer stock;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private List<String> categories;

    private Boolean deleted;

    private ProductStatus status;

    @Builder
    public ProductCreateCommand(String name, Integer stock, String imageUrl, String description, BigDecimal price, String status, List<String> categories) {
        this.name = name;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.deleted = false;
        this.status = validateInitialState(status);
    }

    public Product toEntity(List<Category> categories) {

        return Product.builder()
                .name(this.name)
                .stock(this.stock)
                .basicPrice(this.price)
                .status(this.status)
                .deleted(this.deleted)
                .categories(categories)
                .build();
    }
}
