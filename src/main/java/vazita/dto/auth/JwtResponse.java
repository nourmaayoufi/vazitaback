package vazita.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for JWT authentication response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private String username;
    private String centerId;
    private String role; // Changed to match roleCode as a string
    private long expiryTime;
    
    public JwtResponse(String token, String refreshToken, String username, String centerId, String role, long expiryTime) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.centerId = centerId;
        this.role = role;
        this.expiryTime = expiryTime;
    }
}
