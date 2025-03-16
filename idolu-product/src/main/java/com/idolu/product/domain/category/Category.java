package com.idolu.product.domain.category;

import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Builder
@Table("category")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Category extends BaseEntity {

    @Id
    @Column("category_id")
    private Long id;

    private String categoryCode;

    private String categoryName;

    private Boolean deleted;
}
