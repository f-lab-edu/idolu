package com.idolu.user.global.utils;

import com.idolu.user.presentation.user.response.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;

    @Value("${jwt.expire.access-token}")
    private int accessTokenExpireHour;

    @Value("${jwt.expire.refresh-token}")
    private int refreshTokenExpireHour;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String resolveAccessToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (!StringUtils.isBlank(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String resolveRefreshToken(ServerHttpRequest request) {
        return Optional.ofNullable(request.getCookies().getFirst("refreshToken"))
                .map(HttpCookie::getValue)
                .orElse(null);
    }

    public Long getUserId(String accessToken) {
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(accessToken);

        Object userId = claims.getPayload().get("userId");

        if (Objects.isNull(userId)) {
            throw new IllegalArgumentException("권한 정보가 없는 토큰입니다.");
        }

        return Long.valueOf(userId.toString());
    }

    // 토큰 생성
    public TokenDto createNewToken(Long userId) {
        String accessToken = getToken(userId, Duration.ofHours(accessTokenExpireHour));
        String refreshToken = getToken(userId, Duration.ofHours(refreshTokenExpireHour));

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 토큰 검증 메서드
    public boolean validateToken(String accessToken) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(accessToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String getToken(Long userId, Duration expiredAt) {
        Date now = new Date();
        Instant instant = now.toInstant();

        return Jwts.builder()
                .claim("userId", userId)
                .issuedAt(now)
                .expiration(Date.from(instant.plus(expiredAt)))
                .signWith(secretKey)
                .compact();
    }
}
