package com.idolu.product.domain.category;

import com.idolu.product.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Table("category")
@EqualsAndHashCode(of = "categoryId", callSuper = false)
public class Category extends BaseEntity {

    @Id
    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private Boolean deleted;

    @Builder
    public Category(LocalDateTime createdAt, LocalDateTime updatedAt, Long categoryId, String categoryCode, String categoryName, Boolean deleted) {
        super(createdAt, updatedAt);
        this.categoryId = categoryId;
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.deleted = deleted;
    }
}
