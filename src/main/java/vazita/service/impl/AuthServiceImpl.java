package vazita.service.impl;

import vazita.dto.auth.JwtResponse;
import vazita.dto.auth.LoginRequest;
import vazita.dto.auth.TokenRefreshRequest;
import vazita.entity.Center;
import vazita.entity.User;
import vazita.exception.ResourceNotFoundException;
import vazita.repository.CenterRepository;
import vazita.repository.UserRepository;
import vazita.security.BlackListService;
import vazita.security.JwtTokenProvider;
import vazita.service.AuthService;
import vazita.util.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final BlackListService blackListService;

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Invalid username or password") {};
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Retrieve user and validate their status (active check)
        User user = userRepository.findByIdUser(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        LocalDate currentDate = LocalDate.now();
        if (!"A".equals(user.getStatus()) || 
            (user.getEndDate() != null && currentDate.isAfter(user.getEndDate())) || 
            (user.getStartDate() != null && currentDate.isBefore(user.getStartDate()))) {
            throw new AuthenticationException("User account is inactive or expired") {};
        }

        // Get the user's center details for routing
        Center center = centerRepository.findById(user.getCenterId())
                .orElseThrow(() -> new ResourceNotFoundException("Center not found"));
        
        // Set the datasource context based on the user's center
        DataSourceContextHolder.setCenterId(center.getIdCentre());

        // Generate JWT token with the user's details
        Authentication userDetails = (Authentication) authentication.getPrincipal();
        String token = tokenProvider.generateToken(userDetails, user.getCenterId(), user.getRoleCode());
        
        return JwtResponse.builder()
                .token(token)
                .username(user.getIdUser())
                .role(user.getRoleCode().toString())
                .centerId(user.getCenterId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public JwtResponse refreshToken(TokenRefreshRequest refreshRequest) {
        String token = refreshRequest.getToken();
        
        // Validate the provided token
        if (!tokenProvider.validateToken(token)) {
            throw new AuthenticationException("Invalid or expired token") {};
        }

        // Check if the token is blacklisted
        if (blackListService.isBlacklisted(token)) {
            throw new AuthenticationException("Token has been invalidated") {};
        }

        // Extract the username from the token
        String username = tokenProvider.getUsernameFromToken(token);
        
        // Fetch the user details
        User user = userRepository.findByIdUser(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        // Generate a new refresh token
        String newToken = tokenProvider.refreshToken(token);

        return JwtResponse.builder()
                .token(newToken)
                .username(user.getIdUser())
                .role(user.getRoleCode().toString())
                .centerId(user.getCenterId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public void logout(String token) {
        // Blacklist the token to prevent further usage
        blackListService.blacklistToken(token);
        
        // Clear the security context
        SecurityContextHolder.clearContext();
    }
}
