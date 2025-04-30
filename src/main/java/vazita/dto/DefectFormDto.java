package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for defect form submission
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectFormDto {
    @NotBlank
    private String nDossier;
    
    @NotBlank
    private String numChassis;
    
    @NotBlank
    private String codeChapitre;
    
    @NotBlank
    private String codePoint;
    
    @NotBlank
    private String codeAlteration;
    
    private String numCentre;
    private String matAgent;
}
