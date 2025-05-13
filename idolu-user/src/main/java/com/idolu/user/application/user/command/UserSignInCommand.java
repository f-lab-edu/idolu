package com.idolu.user.application.user.command;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSignInCommand {

    private String email;
    private String password;

    @Builder
    public UserSignInCommand(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
