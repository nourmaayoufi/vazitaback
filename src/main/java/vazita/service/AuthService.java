package vazita.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for handling authentication operations in a private network.
 * Supports login, logout, and token refresh with Redis session management.
 * Uses plaintext passwords as per UTILISATEURS.PASSE requirements.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Authenticates a user and generates a JWT token.
     * @param loginRequest The login request with user ID and password.
     * @return AuthResponse containing the JWT token and user details.
     * @throws AuthenticationException If authentication fails.
     */
    public AuthResponse login(AuthRequest loginRequest) {
        log.debug("Authenticating user: {}", loginRequest.getIdUser());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getIdUser(),
                        loginRequest.getPasse()
                )
        );
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtil.generateJwtToken(authentication);
        UserSessionDto session = UserSessionDto.builder()
                .idUser(userDetails.getUsername())
                .role(userDetails.getCodGrp())
                .idCentre(userDetails.getIdCentre())
                .tokenId(jwt)
                .lastAccess(LocalDateTime.now())
                .expirationMs(jwtUtil.getJwtExpirationMs())
                .build();
        tokenBlacklistService.cacheUserSession(userDetails.getUsername(), session);
        log.info("User authenticated: {}", loginRequest.getIdUser());
        return AuthResponse.builder()
                .idUser(userDetails.getUsername())
                .role(userDetails.getCodGrp())
                .idCentre(userDetails.getIdCentre())
                .jwt(jwt)
                .expirationMs(jwtUtil.getJwtExpirationMs())
                .build();
    }

    /**
     * Refreshes a valid JWT token and updates the user session.
     * @param request The HTTP request containing the Authorization header.
     * @return AuthResponse with the refreshed JWT token.
     * @throws IllegalArgumentException If the token or session is invalid.
     */
    public AuthResponse refreshToken(HttpServletRequest request) {
        log.debug("Refreshing token");
        String jwt = parseJwt(request);
        if (!jwtUtil.validateJwtToken(jwt)) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
        String idUser = jwtUtil.getUsernameFromJwtToken(jwt);
        Optional<UserSessionDto> sessionOpt = tokenBlacklistService.getUserSession(idUser);
        if (sessionOpt.isEmpty() || !sessionOpt.get().getTokenId().equals(jwt)) {
            throw new IllegalArgumentException("Invalid or expired session");
        }
        String refreshedJwt = jwtUtil.refreshJwtToken(jwt);
        if (refreshedJwt == null) {
            throw new IllegalArgumentException("Unable to refresh token");
        }
        UserSessionDto session = sessionOpt.get();
        session.setTokenId(refreshedJwt);
        session.setLastAccess(LocalDateTime.now());
        session.setExpirationMs(jwtUtil.getJwtExpirationMs());
        tokenBlacklistService.cacheUserSession(idUser, session);
        log.info("Token refreshed for user: {}", idUser);
        return AuthResponse.builder()
                .idUser(idUser)
                .role(session.getRole())
                .idCentre(session.getIdCentre())
                .jwt(refreshedJwt)
                .expirationMs(jwtUtil.getJwtExpirationMs())
                .build();
    }

    /**
     * Logs out a user by blacklisting their JWT token and clearing the session.
     * @param request The HTTP request containing the Authorization header.
     * @return A success message.
     * @throws IllegalArgumentException If the token is invalid.
     */
    public String logout(HttpServletRequest request) {
        log.debug("Logging out user");
        String jwt = parseJwt(request);
        if (!jwtUtil.validateJwtToken(jwt)) {
            throw new IllegalArgumentException("Invalid or expired JWT token");
        }
        String idUser = jwtUtil.getUsernameFromJwtToken(jwt);
        tokenBlacklistService.blacklistToken(jwt, jwtUtil.getJwtExpirationMs());
        tokenBlacklistService.clearUserSession(idUser);
        log.info("User logged out: {}", idUser);
        return "Logout successful";
    }

    /**
     * Parses the JWT from the Authorization header.
     * @param request The HTTP request.
     * @return The JWT token.
     * @throws IllegalArgumentException If the header is invalid.
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid Authorization header");
        }
        return headerAuth.substring(7);
    }
}