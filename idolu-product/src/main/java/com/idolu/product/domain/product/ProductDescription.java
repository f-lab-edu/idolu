package com.idolu.product.domain.product;

import com.idolu.product.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "productDescriptionId", callSuper = false)
public class ProductDescription extends BaseEntity {

    @Id
    private Long productDescriptionId;

    private Long productId;

    private String title;

    private String content;

    private Integer sortNumber;

    private Boolean deleted = false;
}
