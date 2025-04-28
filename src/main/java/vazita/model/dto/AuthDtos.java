package vazita.model.dto;

// Import necessary classes
import jakarta.validation.constraints.NotBlank;  // For validation (e.g., ensuring a field is not blank)
import jakarta.validation.constraints.Size;     // For validation (e.g., checking the size of a string)
import lombok.AllArgsConstructor;             // For generating constructor with all fields
import lombok.Builder;                       // For generating builder pattern methods
import lombok.Data;                          // For generating getters, setters, toString, equals, and hashCode
import lombok.NoArgsConstructor;             // For generating no-argument constructor

// This class contains nested DTO classes related to authentication
public class AuthDtos {

    // DTO class for the login request
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthRequestDto {
        // The username field is required and cannot be blank
        @NotBlank(message = "Username cannot be empty")
        private String username;
        
        // The password field is required and cannot be blank
        @NotBlank(message = "Password cannot be empty")
        private String password;
    }
    
    // DTO class for the authentication response, after successful login
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuthResponseDto {
        // Access token returned after successful login
        private String accessToken;
        
        // Refresh token used to obtain a new access token when expired
        private String refreshToken;
        
        // The username of the authenticated user
        private String username;
        
        // The role assigned to the user (e.g., Admin, Inspector)
        private String role;
        
        // The ID of the center associated with the user
        private Integer centerId;
        
        // First name of the user
        private String firstName;
        
        // Last name of the user
        private String lastName;
        
        // Arabic first name of the user
        private String firstNameA;
        
        // Arabic last name of the user
        private String lastNameA;
    }
    
    // DTO class for the refresh token request (to get a new access token)
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenRequestDto {
        // The refresh token field is required and cannot be blank
        @NotBlank(message = "Refresh token cannot be empty")
        private String refreshToken;
    }
}
