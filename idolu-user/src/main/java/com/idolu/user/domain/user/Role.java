package com.idolu.user.domain.user;

import com.idolu.user.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@Builder
public class Role extends BaseEntity {

    @Id
    private Long roleId;

    private String name;

    @PersistenceCreator
    public Role(Long roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    @Builder
    public Role(Long roleId, String name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.roleId = roleId;
        this.name = name;
    }
}
