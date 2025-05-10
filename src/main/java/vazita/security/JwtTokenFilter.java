package vazita.security;

import vazita.exception.InvalidTokenException;
import vazita.util.DataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (jwt != null && tokenProvider.validateToken(jwt)) {
                Authentication auth = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(auth);

                // Set the DataSource context based on center ID in token
                Integer centerId = tokenProvider.getCenterIdFromToken(jwt);
                if (centerId != null) {
                    DataSourceContextHolder.setCenterId(centerId);
                }
            }
        } catch (InvalidTokenException e) {
            log.error("Could not set user authentication in security context", e);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            DataSourceContextHolder.clear();
        }
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
