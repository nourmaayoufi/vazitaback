package vazita.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Utility class for Redis operations, particularly for JWT token blacklisting
 * and user session caching.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {

    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:token:";
    private static final String USER_SESSION_PREFIX = "session:user:";
    
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    
    /**
     * Blacklist a JWT token
     * @param token The JWT token to blacklist
     * @param expirationTime Time in seconds until the token expires
     */
    public void blacklistToken(String token, long expirationTime) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        try {
            redisTemplate.opsForValue().set(key, "blacklisted", expirationTime, TimeUnit.SECONDS);
            log.debug("Token blacklisted with expiration of {} seconds", expirationTime);
        } catch (Exception e) {
            log.error("Failed to blacklist token", e);
        }
    }
    
    /**
     * Check if a token is blacklisted
     * @param token The JWT token to check
     * @return true if the token is blacklisted, false otherwise
     */
    public boolean isTokenBlacklisted(String token) {
        String key = TOKEN_BLACKLIST_PREFIX + token;
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            log.error("Failed to check token blacklist status", e);
            // If Redis is down, assume token is not blacklisted to avoid blocking legitimate requests
            return false;
        }
    }
    
    /**
     * Cache user session data
     * @param userId User ID
     * @param sessionData Session data to cache
     * @param expirationTime Time in seconds until the session expires
     */
    public void cacheUserSession(String userId, Object sessionData, long expirationTime) {
        String key = USER_SESSION_PREFIX + userId;
        try {
            objectRedisTemplate.opsForValue().set(key, sessionData, expirationTime, TimeUnit.SECONDS);
            log.debug("User session cached for user {} with expiration of {} seconds", userId, expirationTime);
        } catch (Exception e) {
            log.error("Failed to cache user session for user: " + userId, e);
        }
    }
    
    /**
     * Get cached user session data
     * @param userId User ID
     * @param clazz Class type for deserialization
     * @return The cached session data or null if not found
     */
    public <T> T getUserSession(String userId, Class<T> clazz) {
        String key = USER_SESSION_PREFIX + userId;
        try {
            Object value = objectRedisTemplate.opsForValue().get(key);
            if (value != null && clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            return null;
        } catch (Exception e) {
            log.error("Failed to get user session for user: " + userId, e);
            return null;
        }
    }
    
    /**
     * Remove user session from cache
     * @param userId User ID
     */
    public void removeUserSession(String userId) {
        String key = USER_SESSION_PREFIX + userId;
        try {
            redisTemplate.delete(key);
            log.debug("User session removed for user {}", userId);
        } catch (Exception e) {
            log.error("Failed to remove user session for user: " + userId, e);
        }
    }
    
    /**
     * Set a value with expiration
     * @param key Redis key
     * @param value Value to store
     * @param timeout Expiration time
     * @param unit Time unit for expiration
     */
    public void setValue(String key, String value, long timeout, TimeUnit unit) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout, unit);
        } catch (Exception e) {
            log.error("Failed to set value for key: " + key, e);
        }
    }
    
    /**
     * Get a value
     * @param key Redis key
     * @return Stored value or null if not found
     */
    public String getValue(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Failed to get value for key: " + key, e);
            return null;
        }
    }
    
    /**
     * Delete a key
     * @param key Redis key to delete
     */
    public void deleteKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Failed to delete key: " + key, e);
        }
    }
}