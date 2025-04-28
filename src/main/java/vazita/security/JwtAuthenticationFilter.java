package vazita.security;

import vazita.config.TenantContext;              // For managing multi-tenant context
import vazita.model.Utilisateur;               // For user details
import vazita.service.TokenBlacklistService;   // For checking if the token is blacklisted
import vazita.service.UserSessionService;      // For extending the user session
import jakarta.servlet.FilterChain;            // For filter chain processing
import jakarta.servlet.ServletException;       // For handling servlet exceptions
import jakarta.servlet.http.HttpServletRequest; // For accessing HTTP request details
import jakarta.servlet.http.HttpServletResponse; // For sending HTTP responses
import lombok.RequiredArgsConstructor;         // Lombok annotation to generate constructor with required arguments
import lombok.extern.slf4j.Slf4j;              // Lombok annotation for logging
import org.springframework.lang.NonNull;         // For ensuring non-null parameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // For authentication token
import org.springframework.security.core.context.SecurityContextHolder; // For managing authentication context
import org.springframework.security.core.userdetails.UserDetailsService; // For loading user details
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // For adding details to authentication
import org.springframework.stereotype.Component;    // Marks the class as a Spring Bean
import org.springframework.web.filter.OncePerRequestFilter; // Base class for filters that are executed only once per request

import java.io.IOException;                    // For handling I/O exceptions

// Component annotation makes this class a Spring Bean, so it can be automatically discovered and injected.
@Component
@RequiredArgsConstructor                      // Lombok annotation to automatically generate a constructor with required dependencies
@Slf4j                                         // Lombok annotation to enable logging
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;            // Utility class for extracting and validating JWT tokens
    private final UserDetailsService userDetailsService; // Service for loading user details based on username
    private final TokenBlacklistService tokenBlacklistService; // Service to check if a token is blacklisted
    private final UserSessionService userSessionService;     // Service for managing user sessions

    // The filter's core functionality: this method processes the request and JWT token.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Get the Authorization header from the request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If the Authorization header is not present or doesn't start with "Bearer ", proceed to the next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header
        jwt = authHeader.substring(7);
        
        try {
            // Check if the JWT token is blacklisted
            if (tokenBlacklistService.isBlacklisted(jwt)) {
                log.warn("Blocked blacklisted token access attempt");
                filterChain.doFilter(request, response);
                return;
            }

            // Extract the username from the JWT token
            username = jwtTokenUtil.extractUsername(jwt);
            
            // If a valid username is found and the user is not already authenticated, proceed
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Load the user details using the username
                Utilisateur utilisateur = (Utilisateur) userDetailsService.loadUserByUsername(username);
                
                // Validate the token with the user details
                if (jwtTokenUtil.validateToken(jwt, utilisateur)) {
                    // Set tenant context based on the user's center ID (to support multi-tenancy)
                    if (utilisateur.getIdCentre() != null) {
                        TenantContext.setCurrentTenant(utilisateur.getIdCentre().toString());
                    } else {
                        TenantContext.setCurrentTenant("CENTRAL"); // Default tenant if center ID is not available
                    }

                    // Create an authentication token and set it in the security context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            utilisateur,           // User object
                            null,                  // No credentials (password is not needed here)
                            utilisateur.getAuthorities() // User authorities (roles/permissions)
                    );
                    
                    // Attach additional details to the authentication token
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set the authentication token in the Spring Security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    // Extend the user's session by updating the session expiry time
                    userSessionService.extendSession(username);
                }
            }
        } catch (Exception e) {
            // Log any exception that occurs during JWT processing
            log.error("Failed to process JWT token: {}", e.getMessage());
        }

        // Pass the request and response to the next filter in the chain
        filterChain.doFilter(request, response);
        
        // Clear tenant context after the request is processed (important for multi-tenancy)
        TenantContext.clear();
    }
}
