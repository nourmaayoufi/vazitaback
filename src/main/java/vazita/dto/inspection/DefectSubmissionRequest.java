package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for defect form submission request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectSubmissionRequest {

    @NotBlank(message = "Dossier number cannot be empty")
    private String dossierNumber;

    @NotBlank(message = "Chassis number cannot be empty")
    private String chassisNumber;

    @NotBlank(message = "Registration number cannot be empty")
    private String registrationNumber;

    @NotNull(message = "Defect codes cannot be null")
    @NotEmpty(message = "At least one defect code must be selected")
    private List<String> defectCodes; // Updated to List<String> to align with defect codes format
}
