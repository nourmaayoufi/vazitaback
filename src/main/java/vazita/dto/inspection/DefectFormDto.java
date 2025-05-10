package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for complete defect form data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectFormDto {

    private String dossierNumber;
    private String chassisNumber;
    private String registrationNumber;
    private LocalDateTime inspectionDateTime;
    private String inspectorId;
    private List<String> defectCodes; // Updated to List<String> to align with defect codes format
    private String centerId;
}
