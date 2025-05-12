package com.idolu.user.domain.user;

import com.idolu.user.global.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Builder
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    private Long userId;

    private Long roleId;

    private String username;

    private String password;

    private String email;

    private String phone;

    private Boolean deleted;

    @PersistenceCreator
    public User(Long userId, Long roleId, String username, String password, String email, String phone, Boolean deleted) {
        this.userId = userId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.deleted = deleted;
    }

    @Builder
    public User(Long userId, Long roleId, String username, String password, String email, String phone, Boolean deleted, LocalDateTime createdAt, LocalDateTime updatedAt) {
        super(createdAt, updatedAt);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.roleId = roleId;
        this.deleted = deleted;
    }
}
