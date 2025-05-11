package com.idolu.user.application.user.command;

import com.idolu.user.domain.user.type.RoleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegularUserSignUpCommand {

    private String username;
    private String password;
    private String email;
    private String phone;
    private RoleType role;

    @Builder
    public RegularUserSignUpCommand(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = RoleType.CUSTOMER;
    }
}
