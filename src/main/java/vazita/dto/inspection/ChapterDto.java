package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for chapter data (top level of defect hierarchy)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDto {
    
    private Integer chapterCode;
    private String chapterLabel;
    private List<DefectPointDto> defectPoints; // Optional for nested response
}
