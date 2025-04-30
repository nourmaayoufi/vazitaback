package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for analytics data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsDto {
    private String defectCode;
    private String defectDescription;
    private long count;
    private double percentage;
}