package vazita.service;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;  // RedisTemplate for Redis operations
    
    @Value("${app.redis.token-blacklist-prefix}")
    private String blacklistPrefix;  // Prefix to add before the token in Redis
    
    @Value("${app.redis.token-expiry}")
    private long tokenExpiry;  // Default expiry time for blacklisted tokens

    /**
     * Blacklists a token with a specified expiry time.
     * @param token The JWT token to blacklist
     * @param expiryInSeconds Expiry time in seconds
     */
    public void blacklistToken(String token, long expiryInSeconds) {
        String key = blacklistPrefix + token;  // Key for the token in Redis
        redisTemplate.opsForValue().set(key, "blacklisted", expiryInSeconds, TimeUnit.SECONDS);  // Store token with expiry
    }

    /**
     * Blacklists a token with a default expiry time.
     * @param token The JWT token to blacklist
     */
    public void blacklistToken(String token) {
        blacklistToken(token, tokenExpiry);  // Call with default expiry
    }

    /**
     * Checks if a token is blacklisted in Redis.
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isBlacklisted(String token) {
        String key = blacklistPrefix + token;  // Key to check in Redis
        Boolean hasKey = redisTemplate.hasKey(key);  // Check if the token exists
        return hasKey != null && hasKey;  // Return true if the token is blacklisted
    }
}
