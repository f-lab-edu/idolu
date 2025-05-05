package com.idolu.user.application.user.command;

import com.idolu.user.domain.user.type.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegularUserSignUpCommand {

    private String username;
    private String password;
    private String email;
    private String phone;
    private UserRole role;

    @Builder
    public RegularUserSignUpCommand(String username, String password, String email, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = UserRole.USER;
    }
}
