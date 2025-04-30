package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for defect point data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectPointDto {
    private String codePoint;
    private String libellePoint;
    private String codeChapitre;
}