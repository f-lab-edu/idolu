package com.idolu.product.presentation.product.request;

import com.idolu.product.application.command.ProductCreateCommand;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class ProductCreateRequest {

    private String name;

    private Integer stock;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private String status;

    private List<String> categories;

    public ProductCreateCommand toCommand() {
        return ProductCreateCommand.builder()
                .name(this.name)
                .stock(this.stock)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .price(this.price)
                .status(this.status)
                .categories(categories)
                .build();
    }
}
