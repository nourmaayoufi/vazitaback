package vazita.service;


import vazita.model.dto.AuthDtos;
import vazita.model.dto.AuthDtos.AuthRequestDto;
import vazita.model.dto.AuthDtos.AuthResponseDto;
import vazita.model.dto.UserSessionDto;
import vazita.model.Utilisateur;
import vazita.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;  // Authentication manager to validate user credentials
    private final JwtTokenUtil jwtTokenUtil;                      // JWT utility for token generation and validation
    private final UserSessionService sessionService;              // Service to handle user session management
    private final TokenBlacklistService tokenBlacklistService;    // Service to handle blacklisting of tokens

    // Method to log in a user
    public AuthResponseDto login(AuthRequestDto request) {
        try {
            // Authenticate user by validating credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),  // Username from request
                            request.getPassword()   // Password from request
                    )
            );
            
            Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();  // Get authenticated user

            // Generate a unique token ID for the session
            String tokenId = UUID.randomUUID().toString();

            // Prepare claims for the access token
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", utilisateur.getCodGrp());  // User role
            claims.put("centerId", utilisateur.getIdCentre());  // User's center ID
            claims.put("tokenId", tokenId);  // Unique token ID

            // Generate access and refresh tokens
            String accessToken = jwtTokenUtil.generateToken(utilisateur, claims);
            String refreshToken = jwtTokenUtil.generateRefreshToken(utilisateur);

            // Create session data to store in session store (e.g., Redis)
            UserSessionDto sessionDto = UserSessionDto.builder()
                    .username(utilisateur.getIdUser())  // User's ID
                    .role(utilisateur.getCodGrp())  // User's role
                    .centerId(utilisateur.getIdCentre())  // User's center ID
                    .tokenId(tokenId)  // Unique token ID
                    .lastAccess(LocalDateTime.now())  // Timestamp of the last access
                    .build();

            // Save user session
            sessionService.saveSession(utilisateur.getIdUser(), sessionDto);

            // Return response DTO with tokens and user details
            return AuthResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(utilisateur.getIdUser())
                    .role(utilisateur.getCodGrp())
                    .centerId(utilisateur.getIdCentre())
                    .firstName(utilisateur.getPrenom())
                    .lastName(utilisateur.getNom())
                    .firstNameA(utilisateur.getPrenomA())
                    .lastNameA(utilisateur.getNomA())
                    .build();

        } catch (BadCredentialsException e) {
            // Catch invalid credentials exception and log the failed login attempt
            log.warn("Authentication failed for user: {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    // Method to log out a user by invalidating the token and removing session
    public void logout(String token, UserDetails userDetails) {
        if (token != null && token.startsWith("Bearer ")) {
            // Extract JWT token
            String jwt = token.substring(7);

            // Blacklist the JWT token (to prevent reuse)
            tokenBlacklistService.blacklistToken(jwt);

            // Remove user session from session store (e.g., Redis)
            sessionService.removeSession(userDetails.getUsername());

            // Log the logout action
            log.info("User logged out: {}", userDetails.getUsername());
        }
    }
}
