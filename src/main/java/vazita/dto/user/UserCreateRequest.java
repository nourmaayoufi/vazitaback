package vazita.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "User ID cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_]{3,20}$", message = "User ID must be alphanumeric and between 3-20 characters")
    private String idUser;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    private String lastNameArabic;
    private String firstNameArabic;

    @NotNull(message = "Role code cannot be null")
    private Integer roleCode;

    @NotNull(message = "Center ID cannot be null")
    private Integer centerId;

    private Date startDate;
    private Date endDate;

    private String status = "A"; // Default status
}
