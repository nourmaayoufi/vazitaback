package vazita.controller;


import vazita.dto.analytics.AnalyticsRequest;
import vazita.dto.analytics.AnalyticsResponse;
import vazita.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

/**
 * Controller for analytics dashboard data
 */
@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Get analytics dashboard data
     *
     * @param request analytics filter parameters
     * @return analytics response with chart data
     */
    @PostMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<AnalyticsResponse> getDashboardData(
            @Valid @RequestBody AnalyticsRequest request) {
        return ResponseEntity.ok(analyticsService.getDashboardData(request));
    }

    /**
     * Get most frequent defects
     *
     * @param startDate start date
     * @param endDate end date
     * @param carType optional car type filter
     * @param limit number of results to return (default 10)
     * @return map of defect codes to frequencies
     */
    @GetMapping("/most-frequent-defects")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<Map<String, Integer>> getMostFrequentDefects(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String carType,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getMostFrequentDefects(startDate, endDate, carType, limit));
    }

    /**
     * Get defect trend by time period
     *
     * @param startDate start date
     * @param endDate end date
     * @param interval time interval (DAY, WEEK, MONTH)
     * @return map of time periods to defect counts
     */
    @GetMapping("/defect-trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<Map<String, Integer>> getDefectTrends(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String interval) {
        return ResponseEntity.ok(analyticsService.getDefectTrends(startDate, endDate, interval));
    }

    /**
     * Get defect distribution by chapter
     *
     * @param startDate start date
     * @param endDate end date
     * @return map of chapter codes to defect counts
     */
    @GetMapping("/defect-distribution")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<Map<String, Integer>> getDefectDistribution(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(analyticsService.getDefectDistribution(startDate, endDate));
    }

    /**
     * Export analytics data as CSV
     *
     * @param request analytics filter parameters
     * @return CSV data as string
     */
    @PostMapping("/export")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<String> exportAnalyticsData(
            @Valid @RequestBody AnalyticsRequest request) {
        return ResponseEntity.ok(analyticsService.exportAnalyticsData(request));
    }
}