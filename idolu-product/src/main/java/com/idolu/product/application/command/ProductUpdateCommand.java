package com.idolu.product.application.command;

import com.idolu.product.domain.category.Category;
import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductStatus;
import com.idolu.product.global.common.SelfValidating;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.idolu.product.domain.product.ProductStatus.toProductStatus;

@Getter
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class ProductUpdateCommand extends SelfValidating<ProductCreateCommand> {

    @Min(value = 1, message = "상품 id는 1보다 커야 합니다.")
    private Long productId;

    @NotBlank(message = "상품 이름을 필수입니다.")
    private String name;

    @Min(value = 1, message = "재고는 1보다 커야 합니다.")
    private Integer stock;

    @NotBlank(message = "이미지 url은 필수입니다.")
    private String imageUrl;

    @NotBlank(message = "상품 설명은 필수입니다.")
    private String description;

    @Min(value = 1_000, message = "가격은 1000원보다 커야 합니다.")
    private BigDecimal price;

    private List<String> categories;

    private Boolean deleted;

    private ProductStatus status;

    @NotNull(message = "업데이트 시각은 필수입니다.")
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
        this.validateSelf();
    }
}
