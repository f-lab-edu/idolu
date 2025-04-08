package com.idolu.product.domain.product;

import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "productImageId", callSuper = false)
public class ProductImage extends BaseEntity {

    @Id
    private Long productImageId;

    private Long productId; // 상품 id

    private String imageType; // 이미지 유형

    private String url;

    private Integer sortNumber; // 정렬 순서

    @Builder.Default
    private Boolean deleted = false;
}
