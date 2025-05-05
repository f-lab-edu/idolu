package com.idolu.user.presentation.user;

import com.idolu.user.application.user.UserService;
import com.idolu.user.global.common.ApiResponse;
import com.idolu.user.presentation.user.request.RegularUserSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public Mono<ApiResponse<Long>> signUpRegularUser(@Valid @RequestBody RegularUserSignUpRequest request) {
        return userService.signUp(request.toCommand())
                .map(ApiResponse::ok);
    }
}
