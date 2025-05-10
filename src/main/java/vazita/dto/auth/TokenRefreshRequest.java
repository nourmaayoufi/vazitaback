package vazita.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for token refresh request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {
    
    @NotBlank(message = "Refresh token cannot be empty")
    private String refreshToken;
}
