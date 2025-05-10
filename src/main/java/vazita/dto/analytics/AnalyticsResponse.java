package vazita.dto.analytics;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO for analytics response data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    
    private List<AnalyticsItem> items;
    private long totalCount;
    private Map<String, Object> metadata; // Optional additional metadata
    
    /**
     * Single analytics data item
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyticsItem {
        private String label; // Label (chapter name, point name, etc.)
        private String code; // Code value
        private long count; // Count/frequency
        private double percentage; // Percentage of total
    }
}
