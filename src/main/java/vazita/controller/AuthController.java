package vazita.controller;

import vazita.dto.JwtResponse;
import vazita.dto.LoginRequest;
import vazita.dto.RefreshTokenRequest;
import vazita.entity.Center;
import vazita.entity.User;
import vazita.repository.CenterRepository;
import vazita.repository.UserRepository;
import vazita.security.JwtTokenProvider;
import vazita.security.UserDetailsImpl;
import vazita.util.CenterContextHolder;
import vazita.util.DataSourceRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CenterRepository centerRepository;
    private final JwtTokenProvider tokenProvider;
    private final DataSourceRouter dataSourceRouter;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        CenterContextHolder.setCenterId("CENTRALE");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userId = userDetails.getId();
        String centerId = userDetails.getCenterId();

        String jwt = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(userId);

        setupCenterDataSource(centerId);

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                refreshToken,
                "Bearer",
                userDetails.getId(),
                userDetails.getNom(),
                userDetails.getPrenom(),
                centerId,
                role
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            if (!tokenProvider.validateToken(refreshToken)) {
                return ResponseEntity.badRequest().body("Invalid refresh token");
            }

            String userId = tokenProvider.getUserIdFromToken(refreshToken);
            CenterContextHolder.setCenterId("CENTRALE");

            Optional<User> userOpt = userRepository.findByIdUser(userId);
            if (userOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User user = userOpt.get();
            String newToken = tokenProvider.generateToken(user);
            String newRefreshToken = tokenProvider.generateRefreshToken(userId);
            setupCenterDataSource(user.getIdCentre());

            String roleName = switch (user.getCodGrp()) {
                case 1 -> "ROLE_ADMIN";
                case 2 -> "ROLE_INSPECTOR";
                case 3 -> "ROLE_ADJOINT";
                default -> "ROLE_USER";
            };

            return ResponseEntity.ok(new JwtResponse(
                    newToken,
                    newRefreshToken,
                    "Bearer",
                    user.getIdUser(),
                    user.getNom(),
                    user.getPrenom(),
                    user.getIdCentre(),
                    roleName
            ));
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return ResponseEntity.badRequest().body("Error refreshing token");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body("Invalid Authorization header");
            }

            String token = authHeader.substring(7);
            tokenProvider.blacklistToken(token);

            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            log.info("User logged out: {}", username);
            return ResponseEntity.ok().body("User logged out successfully");

        } catch (Exception e) {
            log.error("Error logging out", e);
            return ResponseEntity.status(500).body("Logout failed");
        }
    }

    private void setupCenterDataSource(String centerId) {
        try {
            CenterContextHolder.setCenterId("CENTRALE");
            Optional<Center> centerOpt = centerRepository.findById(centerId);

            if (centerOpt.isPresent()) {
                Center center = centerOpt.get();
                dataSourceRouter.addCenterDataSource(
                        centerId,
                        center.getMachine(),
                        center.getSid(),
                        center.getUsername(),
                        center.getPassword()
                );
                log.info("Set up data source for center: {}", centerId);
            } else {
                log.warn("Center not found: {}", centerId);
            }
        } catch (Exception e) {
            log.error("Error setting up center data source", e);
        }
    }
}
