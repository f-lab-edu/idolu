package com.idolu.user.application.user;

import ch.qos.logback.core.spi.ErrorCodes;
import com.idolu.user.application.user.command.RegularUserSignUpCommand;
import com.idolu.user.application.user.command.UserSignInCommand;
import com.idolu.user.domain.user.AuthenticatedUser;
import com.idolu.user.domain.user.Role;
import com.idolu.user.domain.user.User;
import com.idolu.user.global.exception.ResponseCode;
import com.idolu.user.global.exception.UserException;
import com.idolu.user.global.utils.JwtTokenProvider;
import com.idolu.user.infrastructure.out.r2dbc.adapter.RoleAdapter;
import com.idolu.user.infrastructure.out.r2dbc.adapter.UserAdapter;
import com.idolu.user.presentation.user.response.ReIssueResponse;
import com.idolu.user.presentation.user.response.TokenDto;
import com.idolu.user.presentation.user.response.UserSignInResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;
import reactor.util.function.Tuples;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;
    private final EncryptService encryptService;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    public Mono<Long> signUp(RegularUserSignUpCommand command) {
        return userAdapter.validateUserNotExists(command.getEmail())
                .then(roleAdapter.findRoleByName(command.getRole().name()))
                .map(role -> toUserEntity(command, role))
                .flatMap(userAdapter::saveUser)
                .map(User::getUserId);
    }

    public Mono<UserSignInResponse> signIn(UserSignInCommand command, ServerHttpResponse response) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(command.getEmail(), command.getPassword());

        return authenticationManager.authenticate(authToken)
                .map(authentication -> {
                    AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
                    TokenDto tokenDto = jwtTokenProvider.createNewToken(authenticatedUser.getUserId());
                    response.addCookie(refreshTokenCookie(tokenDto.getRefreshToken()));
                    return Tuples.of(authenticatedUser, tokenDto);
                })
                .flatMap(TupleUtils.function((authUser, tokenDto) ->
                        tokenService.upsertToken(authUser.getUserId(), tokenDto.getRefreshToken())
                                .then(Mono.just(Tuples.of(authUser, tokenDto)))))
                .map(TupleUtils.function(UserSignInResponse::from));

    }

    public Mono<ReIssueResponse> reissue(ServerHttpRequest request, ServerHttpResponse response) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if (!StringUtils.hasText(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            return Mono.error(new UserException(ResponseCode.INVALID_REFRESH_TOKEN));
        }

        return userAdapter.findUserBydId(jwtTokenProvider.getUserId(refreshToken))
                .flatMap(user -> tokenService.existsByRefreshToken(user.getUserId())
                        .then(Mono.just(user)))
                .map(user -> {
                    TokenDto tokenDto = jwtTokenProvider.createNewToken(user.getUserId());
                    response.addCookie(refreshTokenCookie(tokenDto.getRefreshToken()));
                    return Tuples.of(user, tokenDto);
                })
                .flatMap(TupleUtils.function((user, tokenDto) ->
                        tokenService.upsertToken(user.getUserId(), tokenDto.getRefreshToken())
                                .then(Mono.just(Tuples.of(user, tokenDto)))))
                .map(TupleUtils.function(ReIssueResponse::from));
    }

    private User toUserEntity(RegularUserSignUpCommand command, Role role) {
        return User.builder()
                .roleId(role.getRoleId())
                .email(command.getEmail())
                .password(encryptService.encrypt(command.getPassword()))
                .username(command.getUsername())
                .phone(command.getPhone())
                .build();
    }

    private ResponseCookie refreshTokenCookie(String refreshToken) {
        return ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(30))
                .build();
    }
}
