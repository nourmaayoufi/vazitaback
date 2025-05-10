package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for car dossier data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDossierDto {

    private String nDossier;
    private String numChassis;
    private String immatriculation;
    private String cPiste;
    private LocalDateTime dateHeureEnregistrement;
    private Long queuePosition; // Optional field for queue display
}
