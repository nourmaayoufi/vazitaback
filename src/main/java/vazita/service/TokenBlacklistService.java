package vazita.service;



import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

	 private final RedisTemplate<String, String> redisTemplate;
	    
	    private static final String TOKEN_BLACKLIST_PREFIX = "blacklist:";
	    private static final String SESSION_PREFIX = "session:";

	    public void blacklistToken(String token) {
	        redisTemplate.opsForValue().set(TOKEN_BLACKLIST_PREFIX + token, "blacklisted", 7, TimeUnit.DAYS);
	    }
	    
	    public boolean isTokenBlacklisted(String token) {
	        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + token));
	    }
	    
	    public void storeUserSession(String username, String token) {
	        redisTemplate.opsForValue().set(SESSION_PREFIX + username, token, 1, TimeUnit.DAYS);
	    }
	    
	    public String getUserSession(String username) {
	        return redisTemplate.opsForValue().get(SESSION_PREFIX + username);
	    }
	    
	    public void invalidateUserSession(String username) {
	        String token = getUserSession(username);
	        if (token != null) {
	            blacklistToken(token);
	            redisTemplate.delete(SESSION_PREFIX + username);
	        }
	    }
	}