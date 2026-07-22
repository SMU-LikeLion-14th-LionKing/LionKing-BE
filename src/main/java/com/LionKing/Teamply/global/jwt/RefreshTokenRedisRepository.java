package com.LionKing.Teamply.global.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

    private static final String KEY_PREFIX = "refreshToken:";

    private final RedisTemplate<String, String> redisTemplate;

    public void save(Long userId, String refreshToken, long expirationMs) {
        redisTemplate.opsForValue().set(
                KEY_PREFIX + userId,
                refreshToken,
                Duration.ofMillis(expirationMs)
        );
    }

    public Optional<String> findByUserId(Long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(KEY_PREFIX + userId));
    }

    public void deleteByUserId(Long userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }
}