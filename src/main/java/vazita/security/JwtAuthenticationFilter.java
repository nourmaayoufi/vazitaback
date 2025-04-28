package vazita.security;

import jakarta.servlet.FilterChain;            // For filter chain processing
import jakarta.servlet.ServletException;       // For handling servlet exceptions
import jakarta.servlet.http.HttpServletRequest; // For accessing HTTP request details
import jakarta.servlet.http.HttpServletResponse; // For sending HTTP responses
import lombok.RequiredArgsConstructor;         // Lombok annotation to generate constructor with required arguments
        // For ensuring non-null parameters
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // For authentication token
import org.springframework.security.core.context.SecurityContextHolder; // For managing authentication context
import org.springframework.security.core.userdetails.UserDetailsService; // For loading user details
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // For adding details to authentication
import org.springframework.stereotype.Component;    // Marks the class as a Spring Bean
import org.springframework.web.filter.OncePerRequestFilter; // Base class for filters that are executed only once per request

import java.io.IOException;                    // For handling I/O exceptions


import vazita.config.DataSourceRouter;
import vazita.service.TokenBlacklistService;



@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String centerId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
                centerId = jwtUtil.extractCenterId(jwt);
            } catch (Exception e) {
                logger.error("Could not extract username from token", e);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Check if token is blacklisted
            if (tokenService.isTokenBlacklisted(jwt)) {
                logger.warn("Attempt to use blacklisted token for user: " + username);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            
            UserDetailsService userDetails = userDetailsServiceImpl.loadUserByUsername(username);
            
            if (jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                
                // Set the current center for database routing
                if (centerId != null) {
                    DataSourceRouter.setCurrentCenter(centerId);
                }
            }
        }
        
        filterChain.doFilter(request, response);
        
        // Clear center ID from thread local after request processing
        DataSourceRouter.clear();
    }
}