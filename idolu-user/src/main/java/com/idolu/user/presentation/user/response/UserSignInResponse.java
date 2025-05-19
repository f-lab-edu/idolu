package com.idolu.user.presentation.user.response;

import com.idolu.user.domain.user.AuthenticatedUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSignInResponse {

    private String username;
    private String email;
    private String phone;
    private String accessToken;

    public static UserSignInResponse from(AuthenticatedUser user, TokenDto tokenDto) {
        return UserSignInResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .accessToken(tokenDto.getAccessToken())
                .build();
    }
}
