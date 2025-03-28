package com.idolu.product.application.command;

import com.idolu.product.domain.product.ProductStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.idolu.product.domain.product.ProductStatus.toProductStatus;

@Getter
public class ProductUpdateCommand {

    private Long productId;

    private String name;

    private Integer stock;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private List<String> categories;

    private Boolean deleted;

    private ProductStatus status;

    private LocalDateTime updatedAt;

    @Builder
    public ProductUpdateCommand(Long productId, String name, Integer stock, String imageUrl, String description, BigDecimal price, List<String> categories, Boolean deleted, String status, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.deleted = deleted;
        this.status = toProductStatus(status);
        this.updatedAt = updatedAt;
    }
}
