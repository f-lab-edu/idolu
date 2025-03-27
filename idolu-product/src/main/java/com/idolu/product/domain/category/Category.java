package com.idolu.product.domain.category;

import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@SuperBuilder
@Table("category")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "categoryId", callSuper = false)
public class Category extends BaseEntity {

    @Id
    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private Boolean deleted;
}
