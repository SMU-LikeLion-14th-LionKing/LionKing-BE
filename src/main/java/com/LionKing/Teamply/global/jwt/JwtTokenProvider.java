package com.LionKing.Teamply.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String TOKEN_TYPE_CLAIM = "type";
    private static final String EMAIL_CLAIM = "email";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(
                java.util.Base64.getEncoder().encodeToString(secret.getBytes())
        );
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    /**
     * Access Token 생성
     */
    public String createAccessToken(Long userId, String email) {
        return createToken(userId, email, ACCESS_TOKEN_TYPE, accessTokenExpiration);
    }

    /**
     * Refresh Token 생성
     */
    public String createRefreshToken(Long userId, String email) {
        return createToken(userId, email, REFRESH_TOKEN_TYPE, refreshTokenExpiration);
    }

    private String createToken(Long userId, String email, String type, long expirationMs) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim(EMAIL_CLAIM, email)
                .claim(TOKEN_TYPE_CLAIM, type)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 userId 추출
     */
    public Long getUserId(String token) {
        return Long.parseLong(parseClaims(token).getSubject());
    }

    /**
     * 토큰에서 email 추출
     */
    public String getEmail(String token) {
        return parseClaims(token).get(EMAIL_CLAIM, String.class);
    }

    /**
     * Access Token 여부 확인
     */
    public boolean isAccessToken(String token) {
        return ACCESS_TOKEN_TYPE.equals(parseClaims(token).get(TOKEN_TYPE_CLAIM, String.class));
    }

    /**
     * Refresh Token 여부 확인
     */
    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(parseClaims(token).get(TOKEN_TYPE_CLAIM, String.class));
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.warn("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.warn("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    /**
     * Claims 파싱 (만료된 경우에도 Claims 반환)
     */
    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
