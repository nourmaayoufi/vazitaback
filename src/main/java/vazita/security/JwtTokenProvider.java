package vazita.security;


import vazita.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Component for JWT token generation, validation, and management
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshExpiration;
    
    private Key key;
    
    @PostConstruct
    public void init() {
        // Generate a strong key for HMAC-SHA signing
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }
    
    /**
     * Generate JWT token from authentication object
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpiration);
        
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("id", userDetails.getId())
                .claim("roles", authorities)
                .claim("centerId", userDetails.getCenterId())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }
    
    /**
     * Generate JWT token from user entity
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpiration);
        
        return Jwts.builder()
                .setSubject(user.getIdUser())
                .claim("id", user.getIdUser())
                .claim("roles", "ROLE_" + user.getGroup().getDesignation())
                .claim("centerId", user.getIdCentre())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
    }
    
    /**
     * Generate refresh token
     */
    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + refreshExpiration);
        
        String refreshToken = Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key)
                .compact();
        
        // Store refresh token in Redis with expiration
        redisTemplate.opsForValue().set(
                "refresh_token:" + userId,
                refreshToken,
                refreshExpiration,
                TimeUnit.MILLISECONDS
        );
        
        return refreshToken;
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            // Check if token is blacklisted
            if (redisTemplate.hasKey("blacklist:" + token)) {
                log.warn("Attempt to use blacklisted token");
                return false;
            }
            
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get user ID from JWT token
     */
    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.getSubject();
    }
    
    /**
     * Get center ID from JWT token
     */
    public String getCenterIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        return claims.get("centerId", String.class);
    }
    
    /**
     * Blacklist a token by adding it to Redis
     */
    public void blacklistToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            Date expiration = claims.getExpiration();
            long ttl = expiration.getTime() - System.currentTimeMillis();
            
            if (ttl > 0) {
                redisTemplate.opsForValue().set(
                        "blacklist:" + token,
                        "1",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
                log.info("Token blacklisted until expiration");
            }
        } catch (Exception e) {
            log.error("Error blacklisting token: {}", e.getMessage());
        }
    }
}