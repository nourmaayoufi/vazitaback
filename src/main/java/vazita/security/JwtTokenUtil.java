package vazita.security;

import io.jsonwebtoken.Claims;              // For accessing claims in the JWT
import io.jsonwebtoken.Jwts;                // For building and parsing JWTs
import io.jsonwebtoken.SignatureAlgorithm; // For signing the JWT with an algorithm
import io.jsonwebtoken.security.Keys;      // For creating secure keys for signing JWT
import jakarta.annotation.PostConstruct;    // For initializing the key after construction
import lombok.RequiredArgsConstructor;     // Lombok annotation for constructor injection
import org.springframework.beans.factory.annotation.Value; // For injecting values from configuration
import org.springframework.security.core.userdetails.UserDetails; // For user authentication details
import org.springframework.stereotype.Component; // To mark the class as a Spring Bean

import java.security.Key;                    // For the signing key
import java.util.Date;                       // For setting expiration dates
import java.util.HashMap;                    // For creating the claims map
import java.util.Map;                        // For using maps to hold claims
import java.util.function.Function;          // For functional programming in claims extraction

// Marks this class as a Spring component (Bean) and automatically injects required dependencies
@Component
@RequiredArgsConstructor                     // Lombok annotation to automatically generate constructor
public class JwtTokenUtil {

    // Reading JWT secret and expiration values from application properties
    @Value("${jwt.secret}")
    private String secret;                      // The secret key used to sign the JWT
    
    @Value("${jwt.expiration}")
    private long jwtExpiration;                 // Expiration time for regular JWT tokens (in milliseconds)
    
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;             // Expiration time for refresh tokens (in milliseconds)

    private Key key;                            // Key used for signing the JWT

    // PostConstruct annotation to initialize the signing key after the constructor is called
    @PostConstruct
    public void init() {
        // Initialize the HMAC SHA key using the provided secret (bytes of the secret string)
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Method to generate a regular JWT token for a user
    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        // Calls the private generateToken method with the user details and expiration time
        return generateToken(claims, userDetails.getUsername(), jwtExpiration);
    }

    // Method to generate a refresh JWT token for a user
    public String generateRefreshToken(UserDetails userDetails) {
        // Calls the private generateToken method with an empty claims map and refresh expiration time
        return generateToken(new HashMap<>(), userDetails.getUsername(), refreshExpiration);
    }

    // Private method to generate a JWT with custom claims, subject, and expiration time
    private String generateToken(Map<String, Object> claims, String subject, long expiration) {
        // Building and signing the JWT with claims, subject, issue date, expiration time, and signature
        return Jwts.builder()
                .setClaims(claims)                           // Set custom claims (optional)
                .setSubject(subject)                         // Set the subject (usually the username)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set the issued date (current time)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Set expiration time
                .signWith(key, SignatureAlgorithm.HS512)    // Sign the token with the key and the HS512 algorithm
                .compact();                                 // Build and return the token as a string
    }

    // Method to validate the JWT token by checking if it is expired and if the username matches
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);  // Extract the username from the token
        // Validate that the username matches and the token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Method to extract the username (subject) from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);  // Extract and return the subject (username)
    }

    // Method to extract the expiration date from the JWT token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);  // Extract and return the expiration date
    }

    // Generic method to extract any claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // Extract all claims from the token
        return claimsResolver.apply(claims);             // Apply the function to get the specific claim
    }

    // Private method to extract all claims from the JWT token
    private Claims extractAllClaims(String token) {
        // Parse the JWT token and extract the claims body
        return Jwts.parserBuilder()
                .setSigningKey(key)  // Set the signing key for validation
                .build()
                .parseClaimsJws(token)  // Parse the token into claims
                .getBody();             // Return the claims body
    }

    // Method to check if the token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Check if the expiration date is before the current date
    }
}
