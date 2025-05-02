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

    private String group1;

    private String group2;

    private Boolean deleted;

    @Builder
    public Category(LocalDateTime createdAt, LocalDateTime updatedAt, Long categoryId, String group1, String group2, Boolean deleted) {
        super(createdAt, updatedAt) ;
        this.categoryId = categoryId;
        this.group1 = group1;
        this.group2 = group2;
        this.deleted = deleted;
    }
}
