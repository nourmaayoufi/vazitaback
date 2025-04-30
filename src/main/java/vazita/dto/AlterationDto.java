package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for alteration data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlterationDto {
    private String codeAlteration;
    private String libelleAlteration;
    private String codeChapitre;
    private String codePoint;
}
