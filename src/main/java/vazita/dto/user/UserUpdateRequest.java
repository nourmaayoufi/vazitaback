package vazita.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    @NotBlank(message = "First name cannot be empty")
    private String firstName;

    private String lastNameArabic;
    private String firstNameArabic;

    @NotNull(message = "Role code cannot be null")
    private Integer roleCode;

    private String password; // Optional

    private Date startDate;
    private Date endDate;

    private String status;

    @NotNull(message = "Center ID cannot be null")
    private Integer centerId;
}
