package com.idolu.user.application.user;

import com.idolu.user.global.authentication.JwtTokenProvider;
import com.idolu.user.infrastructure.out.r2dbc.adapter.RoleAdapter;
import com.idolu.user.infrastructure.out.r2dbc.adapter.UserAdapter;
import com.idolu.user.infrastructure.out.redis.RedisAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.function.TupleUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.expire.refresh-token}")
    private Long refreshTokenExpireHour;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisAdapter redisAdapter;
    private final UserAdapter userAdapter;
    private final RoleAdapter roleAdapter;

    private static final String BASE_KEY = "auth:";


    public Mono<Boolean> upsertToken(String email, String refreshToken) {
        String key = BASE_KEY + email;

        return redisAdapter.setValue(key, refreshToken, refreshTokenExpireHour * 60 * 60);
    }

    public Mono<Boolean> existsByRefreshToken(String email) {
        return redisAdapter.getValue(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.")))
                .then(Mono.just(true));
    }

    public Mono<Authentication> getAuthentication(String accessToken) {
        return userAdapter.findUserBydId(jwtTokenProvider.getUserId(accessToken))
                .zipWhen(user -> roleAdapter.findRoleById(user.getRoleId()))
                .map(TupleUtils.function((user, role) -> {
                    List<SimpleGrantedAuthority> simpleGrantedAuthorities = List.of(new SimpleGrantedAuthority(role.getName()));
                    return new UsernamePasswordAuthenticationToken(
                            new User(user.getUsername(), user.getPassword(), simpleGrantedAuthorities),
                            user.getUserId(),
                            simpleGrantedAuthorities);
                }));
    }
}
