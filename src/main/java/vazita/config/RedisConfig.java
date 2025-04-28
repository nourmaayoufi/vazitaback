package vazita.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Read Redis host and port from application properties
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    // Read custom Redis-related properties
    @Value("${app.redis.token-blacklist-prefix}")
    private String tokenBlacklistPrefix;

    @Value("${app.redis.user-session-prefix}")
    private String userSessionPrefix;

    @Value("${app.redis.token-expiry}")
    private long tokenExpiry;

    @Value("${app.redis.session-expiry}")
    private long sessionExpiry;

    // Create a connection factory to connect to Redis server
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    // Create a RedisTemplate for interacting with Redis
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    // Expose custom Redis settings as beans
    @Bean
    public String tokenBlacklistPrefix() {
        return tokenBlacklistPrefix;
    }

    @Bean
    public String userSessionPrefix() {
        return userSessionPrefix;
    }

    @Bean
    public long tokenExpiry() {
        return tokenExpiry;
    }

    @Bean
    public long sessionExpiry() {
        return sessionExpiry;
    }
}
