package com.idolu.product.presentation.product.request;

import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductStatus;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductCreateRequest {
    private String name;
    private Integer stock;
    private String imageUrl;
    private String description;
    private BigDecimal price;
    private String status;

    public Product toEntity() {
        return Product.builder()
                .name(this.name)
                .stock(this.stock)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .price(this.price)
                .status(ProductStatus.valueOf(this.status))
                .build();
    }
}
