package vazita.dto.analytics;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO for analytics request parameters
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsRequest {
    
    private LocalDate startDate;
    private LocalDate endDate;
    private String carType; // Optional filter by car type (chassis pattern)
    private String centerId; // Optional filter by center ID
    private Integer chapterCode; // Optional filter by chapter
    private String groupBy; // What to group results by: "chapter", "point", "alteration", "day", "month"
    private Integer limit = 10; // Limit number of results
    private String sortBy = "count"; // Sort by "count" or "code"
    private String sortOrder = "desc"; // "asc" or "desc"
}
