package com.nchl.authorization_server.service.impl;

import com.nchl.authorization_server.model.OAuthToken;
import com.nchl.authorization_server.service.IOAuthTokenCacheService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@AllArgsConstructor
public class OAuthTokenCacheService implements IOAuthTokenCacheService {

    private static final String TOKEN_PREFIX = "oauth:token:";

    private final RedisTemplate<String, OAuthToken> redisTemplate;

    public void storeToken(String userId, OAuthToken token) {
        redisTemplate.opsForValue().set(
                TOKEN_PREFIX + userId,
                token,
                Duration.ofSeconds(token.getExpiresIn()) // expires automatically
        );
    }

    public OAuthToken getToken(String userId) {
        return redisTemplate.opsForValue().get(TOKEN_PREFIX + userId);
    }

    public void deleteToken(String userId) {
        redisTemplate.delete(TOKEN_PREFIX + userId);
    }
}
