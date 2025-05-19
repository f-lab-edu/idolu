package com.idolu.user.domain.user.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {
    CUSTOMER("ROLE_CUSTOMER"),
    MANAGER("ROLE_MANAGER"),
    ADMIN("ROLE_ADMIN"),
    ;

    private final String role;
}
