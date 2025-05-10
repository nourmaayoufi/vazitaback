package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for defect point data (middle level of defect hierarchy)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefectPointDto {
    
    private Integer pointCode;
    private String pointLabel;
    private Integer chapterCode;
    private List<AlterationDto> alterations; // Optional for nested response
}