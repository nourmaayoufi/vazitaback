package vazita.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for login request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username cannot be empty")
    private String username;
    
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
