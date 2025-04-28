package vazita.config;

// Import necessary Spring Security and configuration classes
import vazita.security.JwtAuthenticationEntryPoint;
import vazita.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

// Mark this class as a configuration for security
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true) // Enable method-level security (@Secured)
@RequiredArgsConstructor
public class SecurityConfig {

    // Inject needed components
    private final JwtAuthenticationFilter jwtAuthFilter; // Filter to extract JWT from requests
    private final JwtAuthenticationEntryPoint unauthorizedHandler; // Handle 401 Unauthorized errors
    private final UserDetailsService userDetailsService; // To load user info during authentication
    private final PasswordEncoder passwordEncoder; // To encode and verify passwords

    // Define the main security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (since using tokens)
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Enable CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Allow public access to auth endpoints
                        .requestMatchers("/api/health").permitAll() // Allow public access to health check
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No session will be created
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler)) // Use custom handler for unauthorized errors
                .authenticationProvider(authenticationProvider()) // Set authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before default auth filter
                .build();
    }

    // Define the AuthenticationProvider
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService); // Use custom UserDetailsService
        authProvider.setPasswordEncoder(passwordEncoder); // Use PasswordEncoder (BCrypt)
        return authProvider;
    }

    // Define the AuthenticationManager (required by Spring Security)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Define CORS settings (so browser clients/mobile apps can call your APIs)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // Allow any origin (can be restricted)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allowed HTTP methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With")); // Allowed headers in requests
        configuration.setExposedHeaders(List.of("Authorization")); // Headers that client can see
        configuration.setMaxAge(3600L); // Cache preflight request for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
