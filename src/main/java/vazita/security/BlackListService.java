package vazita.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.concurrent.TimeUnit;

@Service
public class BlackListService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;
    
    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    public void blacklistToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        long ttl = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(BLACKLIST_PREFIX + token);
    }
}
