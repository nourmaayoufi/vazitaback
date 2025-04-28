package vazita.model.dto;

// Import necessary classes
import lombok.AllArgsConstructor;  // For generating a constructor with all fields
import lombok.Builder;            // For generating builder pattern methods
import lombok.Data;               // For generating getters, setters, toString, equals, and hashCode
import lombok.NoArgsConstructor;  // For generating no-argument constructor
import java.time.LocalDateTime;    // To store the last access time in a LocalDateTime format

// DTO class to represent a user session
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSessionDto {

    // The username of the user associated with the session
    private String username;
    
    // The role of the user (e.g., Admin, Inspector)
    private String role;
    
    // The ID of the center associated with the user's session
    private Integer centerId;
    
    // A unique token ID representing the session or session-related information
    private String tokenId;
    
    // The last time the user accessed the system (for session expiration or monitoring)
    private LocalDateTime lastAccess;
}
