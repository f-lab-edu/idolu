package com.idolu.user.domain.user;

import com.idolu.user.domain.user.type.UserRole;
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

    private String username;

    private String password;

    private String email;

    private String phone;

    private UserRole role;

    @PersistenceCreator
    public User(Long userId, String username, String password, String email, String phone, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    @Builder
    public User(LocalDateTime createdAt, LocalDateTime updatedAt, Long userId, String username, String password, String email, String phone, UserRole role) {
        super(createdAt, updatedAt);
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }
}
