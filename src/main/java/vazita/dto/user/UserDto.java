package vazita.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO for user data transfer
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String idUser;
    private String lastName;
    private String firstName;
    private String lastNameArabic;
    private String firstNameArabic;
    private Integer roleCode;
    private String groupName;  // e.g. ROLE_ADMIN
    private Integer centerId;
    private Date startDate;
    private Date endDate;
    private String status;
    private boolean active;
}
