package com.idolu.product.domain.product;

import com.idolu.product.domain.product.type.ImageType;
import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@EqualsAndHashCode(of = "productImageId", callSuper = false)
public class ProductImage extends BaseEntity {

    @Id
    private Long productImageId;

    private Long productId; // 상품 id

    private ImageType imageType; // 이미지 유형

    private String url;

    private Integer sortNumber; // 정렬 순서

    private Boolean deleted;

    @Builder
    public ProductImage(Long productImageId, Long productId, ImageType imageType, String url, Integer sortNumber, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.productImageId = productImageId;
        this.productId = productId;
        this.imageType = imageType;
        this.url = url;
        this.sortNumber = sortNumber;
        this.deleted = deleted;
    }

    public ProductImage withProductId(Long productId) {
        this.productId = productId;
        return this;
    }
}
