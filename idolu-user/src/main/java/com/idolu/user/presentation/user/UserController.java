package com.idolu.user.presentation.user;

import com.idolu.user.application.user.UserService;
import com.idolu.user.global.common.ApiResponse;
import com.idolu.user.presentation.user.request.RegularUserSignUpRequest;
import com.idolu.user.presentation.user.request.UserSignInRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;
    private final ReactiveAuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public Mono<ApiResponse<Long>> signUpRegularUser(@Valid @RequestBody RegularUserSignUpRequest request) {
        return userService.signUp(request.toCommand())
                .map(ApiResponse::ok);
    }

    @PostMapping("/login")
    public Mono<ApiResponse<Void>> signIn(@Valid @RequestBody UserSignInRequest request) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        return authenticationManager.authenticate(authToken)
                .log()
                .then(Mono.just(ApiResponse.ok(null)));
    }
}
