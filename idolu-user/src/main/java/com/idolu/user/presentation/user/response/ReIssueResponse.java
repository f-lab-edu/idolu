package com.idolu.user.presentation.user.response;

import com.idolu.user.domain.user.AuthenticatedUser;
import com.idolu.user.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReIssueResponse {

    private String username;
    private String email;
    private String phone;
    private String accessToken;

    public static ReIssueResponse from(User user, TokenDto tokenDto) {
        return ReIssueResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .accessToken(tokenDto.getAccessToken())
                .build();
    }
}
