package vazita.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vazita.model.dto.UserSessionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSessionService {

    private final RedisTemplate<String, String> redisTemplate; // Redis template for managing session data in Redis
    private final ObjectMapper objectMapper; // ObjectMapper to serialize/deserialize session data
    
    @Value("${app.redis.user-session-prefix}")
    private String sessionPrefix; // Prefix for Redis keys to store user sessions
    
    @Value("${app.redis.session-expiry}")
    private long sessionExpiry; // Expiry time for user sessions in Redis (in seconds)

    /**
     * Saves a user session to Redis.
     * 
     * @param username The username associated with the session.
     * @param sessionDto The session data to be saved.
     */
    public void saveSession(String username, UserSessionDto sessionDto) {
        try {
            String key = sessionPrefix + username; // Key format: sessionPrefix + username
            String sessionJson = objectMapper.writeValueAsString(sessionDto); // Serialize the session data to JSON
            redisTemplate.opsForValue().set(key, sessionJson, sessionExpiry, TimeUnit.SECONDS); // Save to Redis with an expiry time
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize user session: {}", e.getMessage()); // Log error if serialization fails
            throw new RuntimeException("Failed to save user session", e); // Throw an exception to indicate failure
        }
    }

    /**
     * Retrieves a user session from Redis.
     * 
     * @param username The username for which the session is to be fetched.
     * @return An Optional containing the session if found, otherwise an empty Optional.
     */
    public Optional<UserSessionDto> getSession(String username) {
        String key = sessionPrefix + username; // Key format: sessionPrefix + username
        String sessionJson = redisTemplate.opsForValue().get(key); // Retrieve session data from Redis
        
        if (sessionJson == null) {
            return Optional.empty(); // Return empty if session data doesn't exist
        }
        
        try {
            UserSessionDto sessionDto = objectMapper.readValue(sessionJson, UserSessionDto.class); // Deserialize the JSON back into a UserSessionDto
            return Optional.of(sessionDto); // Return the session as an Optional
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize user session: {}", e.getMessage()); // Log error if deserialization fails
            return Optional.empty(); // Return empty if deserialization fails
        }
    }

    /**
     * Removes a user session from Redis.
     * 
     * @param username The username of the session to be removed.
     */
    public void removeSession(String username) {
        String key = sessionPrefix + username; // Key format: sessionPrefix + username
        redisTemplate.delete(key); // Delete the session data from Redis
    }

    /**
     * Extends the expiry time of an existing user session in Redis.
     * 
     * @param username The username for which the session expiry is to be extended.
     */
    public void extendSession(String username) {
        String key = sessionPrefix + username; // Key format: sessionPrefix + username
        Boolean hasKey = redisTemplate.hasKey(key); // Check if the session key exists in Redis
        
        if (hasKey != null && hasKey) {
            redisTemplate.expire(key, sessionExpiry, TimeUnit.SECONDS); // Extend the session expiry time if the session exists
        }
    }
}
