package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for queue item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueItemDto {
    private String nDossier;
    private String numChassis;
    private String immatriculation;
    private String cPiste;
    private Date dateHeureEnregistrement;
}
