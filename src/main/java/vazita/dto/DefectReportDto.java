package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for defect report data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectReportDto {
    private Long id;
    private Date datCtrl;
    private String numCentre;
    private String nDossier;
    private Date dateHeureEnregistrement;
    private String numChassis;
    private String codeDefaut;
    private String matAgent;
    
    // Additional fields with description information
    private String libelleDefaut;
    private String inspectorName;
}