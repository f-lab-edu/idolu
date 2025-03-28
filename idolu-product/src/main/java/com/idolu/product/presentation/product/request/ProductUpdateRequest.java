package com.idolu.product.presentation.product.request;

import com.idolu.product.application.command.ProductUpdateCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductUpdateRequest {

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

    @Size(min = 1, message = "상품은 최소 하나의 카테고리에 속해야 합니다.")
    private List<String> categories;

    @NotNull(message = "삭제 여부는 필수입니다.")
    private Boolean deleted;

    @NotBlank(message = "상품 상태값은 필수입니다.")
    private String status;

    @NotNull(message = "업데이트 시각은 필수입니다.")
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
