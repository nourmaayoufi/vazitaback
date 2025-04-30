package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for creating/updating a user
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String idUser;

    @NotBlank
    @Size(min = 3, max = 40)
    private String nom;

    @NotBlank
    @Size(min = 3, max = 40)
    private String prenom;

    private String noma;
    private String prenoma;

    @NotBlank
    private Integer codGrp;

    @NotBlank
    private String password;

    private Date dateDeb;
    private Date dateFin;
    private String etat;
}
