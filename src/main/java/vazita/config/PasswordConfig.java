package vazita.config;

// Import necessary Spring classes
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Mark this class as a configuration class for Spring to scan
@Configuration
public class PasswordConfig {

    // Define a Spring Bean for PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Return a BCryptPasswordEncoder instance
        // BCrypt is a strong hashing algorithm with salt built-in
        return new BCryptPasswordEncoder();
    }
}
