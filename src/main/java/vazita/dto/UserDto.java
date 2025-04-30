package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for user data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String idUser;
    private String nom;
    private String prenom;
    private String noma;
    private String prenoma;
    private Integer codGrp;
    private String roleName;
    private String idCentre;
    private Date dateDeb;
    private Date dateFin;
    private String etat;
    private String password;
}
