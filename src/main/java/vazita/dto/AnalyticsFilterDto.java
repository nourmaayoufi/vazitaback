package vazita.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
/**
 * DTO for analytics filter
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsFilterDto {
    private Date startDate;
    private Date endDate;
    private String carType;
    private String centerId;
}