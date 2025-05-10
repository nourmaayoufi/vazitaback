package vazita.dto.inspection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for alteration data (leaf level of defect hierarchy)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterationDto {

    private Integer alterationCode;
    private String alterationLabel;
    private Integer chapterCode;
    private Integer pointCode;
}
