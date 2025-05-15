package com.idolu.user.presentation.user;

import com.idolu.user.application.user.UserService;
import com.idolu.user.global.common.ApiResponse;
import com.idolu.user.presentation.user.request.RegularUserSignUpRequest;
import com.idolu.user.presentation.user.request.UserSignInRequest;
import com.idolu.user.presentation.user.response.ReIssueResponse;
import com.idolu.user.presentation.user.response.UserSignInResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Mono<ApiResponse<Long>> signUpRegularUser(@Valid @RequestBody RegularUserSignUpRequest request) {
        return userService.signUp(request.toCommand())
                .map(ApiResponse::ok);
    }

    @PostMapping("/signin")
    public Mono<ApiResponse<UserSignInResponse>> signIn(@Valid @RequestBody UserSignInRequest request, ServerHttpResponse response) {
        return userService.signIn(request.toCommand(), response)
                .map(ApiResponse::ok);
    }

    @PostMapping("/reissue")
    public Mono<ApiResponse<ReIssueResponse>> reissue(ServerHttpRequest request, ServerHttpResponse response) {
        return userService.reissue(request, response)
                .map(ApiResponse::ok);
    }
}
