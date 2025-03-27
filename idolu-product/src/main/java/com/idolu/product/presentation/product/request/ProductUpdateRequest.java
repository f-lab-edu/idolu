package com.idolu.product.presentation.product.request;

import com.idolu.product.application.command.ProductCreateCommand;
import com.idolu.product.application.command.ProductUpdateCommand;
import com.idolu.product.global.common.SelfValidating;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductUpdateRequest extends SelfValidating<ProductCreateCommand> {

    private Long productId;

    private String name;

    private Integer stock;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private List<String> categories;

    private Boolean deleted;

    private String status;

    private LocalDateTime updatedAt;

    public ProductUpdateCommand toCommand() {
        return ProductUpdateCommand.builder()
                .productId(this.productId)
                .name(this.name)
                .stock(this.stock)
                .imageUrl(this.imageUrl)
                .description(this.description)
                .price(this.price)
                .categories(this.categories)
                .deleted(this.deleted)
                .status(this.status)
                .updatedAt(this.updatedAt)
                .build();
    }
}
