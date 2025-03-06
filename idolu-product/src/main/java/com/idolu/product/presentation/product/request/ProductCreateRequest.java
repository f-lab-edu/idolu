package com.idolu.product.presentation.product.request;

import com.idolu.product.domain.product.Product;
import com.idolu.product.domain.product.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductCreateRequest {

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

    @NotBlank(message = "상품 초기 상태는 필수입니다")
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
