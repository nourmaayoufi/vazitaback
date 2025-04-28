package vazita.controllers;



import  vazita.model.dto.AuthRequest;
import  vazita.model.dto.AuthResponse;
import  vazita.model.entity.Utilisateur;
import  vazita.repository.UtilisateurRepository;
import  vazita.security.JwtTokenUtil;
import  vazita.service.TokenBlacklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtUtil;
    private final UtilisateurRepository utilisateurRepository;
    private final TokenBlacklistService tokenService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByIdUser(authRequest.getUsername());
        
        if (utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(401).body("User not found");
        }
        
        Utilisateur utilisateur = utilisateurOpt.get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        
        String token = jwtUtil.generateToken(userDetails, utilisateur.getIdCentre(), utilisateur.getCodGrp());
        
        // Store the token in Redis
        tokenService.storeUserSession(authRequest.getUsername(), token);
        
        return ResponseEntity.ok(new AuthResponse(token, utilisateur.getNom(), utilisateur.getPrenom(), 
                utilisateur.getCodGrp(), utilisateur.getIdCentre()));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);
            
            // Invalidate the session and blacklist the token
            tokenService.invalidateUserSession(username);
            return ResponseEntity.ok("Logged out successfully");
        }
        
        return ResponseEntity.badRequest().body("Invalid token");
    }
}