package vazita.service.impl;


import vazita.dto.analytics.AnalyticsRequest;
import vazita.dto.analytics.AnalyticsResponse;
import vazita.repository.DossierDefectRepository;
import vazita.service.AnalyticsService;
import vazita.util.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DossierDefectRepository dossierDefectRepository;
    
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    @Cacheable(value = "analyticsDashboard", key = "#request.centerId + '-' + #request.startDate + '-' + #request.endDate + '-' + #request.carType")
    public AnalyticsResponse getDashboardData(AnalyticsRequest request) {
        LocalDateTime startDate = parseDate(request.getStartDate());
        LocalDateTime endDate = parseDate(request.getEndDate());
        
        // Get most frequent defects by category
        List<Object[]> frequentDefects = dossierDefectRepository.findMostFrequentDefects(
                request.getCenterId(), 
                startDate, 
                endDate,
                request.getCarType()
        );
        
        // Get defect trends over time
        List<Object[]> defectTrends = dossierDefectRepository.findDefectTrendsByPeriod(
                request.getCenterId(),
                startDate,
                endDate,
                request.getCarType()
        );
        
        // Get defect distribution by chapter
        List<Object[]> defectDistribution = dossierDefectRepository.findDefectDistributionByChapter(
                request.getCenterId(),
                startDate,
                endDate,
                request.getCarType()
        );
        
        // Process data and build response
        Map<String, Object> chartData = new HashMap<>();
        chartData.put("frequentDefects", processFrequentDefects(frequentDefects));
        chartData.put("defectTrends", processDefectTrends(defectTrends));
        chartData.put("defectDistribution", processDefectDistribution(defectDistribution));
        
        AnalyticsResponse response = new AnalyticsResponse();
        response.setChartData(chartData);
        response.setTotalInspections(countTotalInspections(request.getCenterId(), startDate, endDate, request.getCarType()));
        response.setTotalDefects(countTotalDefects(request.getCenterId(), startDate, endDate, request.getCarType()));
        
        return response;
    }
    
    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public Map<String, Object> exportAnalyticsData(AnalyticsRequest request) {
        // Implementation for exporting analytics data
        // This would format the same data as getDashboardData but in an exportable format
        AnalyticsResponse dashboardData = getDashboardData(request);
        
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("dashboardData", dashboardData);
        exportData.put("exportTimestamp", LocalDateTime.now().toString());
        exportData.put("exportFormat", "JSON"); // Could be CSV, Excel, etc.
        
        return exportData;
    }
    
    private LocalDateTime parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return LocalDateTime.now().minusMonths(1);
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(dateString, formatter);
            return date.atStartOfDay();
        } catch (Exception e) {
            return LocalDateTime.now().minusMonths(1);
        }
    }
    
    private List<Map<String, Object>> processFrequentDefects(List<Object[]> rawData) {
        // Process raw database results into a format suitable for frontend charts
        // Each map represents a data point with labels and values
        // Implementation depends on specific needs and chart library
        
        // This is a simplified implementation
        return rawData.stream()
                .map(row -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("defectCode", row[0]);
                    dataPoint.put("defectDescription", row[1]);
                    dataPoint.put("count", row[2]);
                    return dataPoint;
                })
                .toList();
    }
    
    private List<Map<String, Object>> processDefectTrends(List<Object[]> rawData) {
        // Process time-based trends
        return rawData.stream()
                .map(row -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("date", row[0]);
                    dataPoint.put("count", row[1]);
                    return dataPoint;
                })
                .toList();
    }
    
    private List<Map<String, Object>> processDefectDistribution(List<Object[]> rawData) {
        // Process distribution data
        return rawData.stream()
                .map(row -> {
                    Map<String, Object> dataPoint = new HashMap<>();
                    dataPoint.put("chapter", row[0]);
                    dataPoint.put("chapterName", row[1]);
                    dataPoint.put("count", row[2]);
                    dataPoint.put("percentage", row[3]);
                    return dataPoint;
                })
                .toList();
    }
    
    private Long countTotalInspections(String centerId, LocalDateTime startDate, LocalDateTime endDate, String carType) {
        return dossierDefectRepository.countDistinctDossiers(centerId, startDate, endDate, carType);
    }
    
    private Long countTotalDefects(String centerId, LocalDateTime startDate, LocalDateTime endDate, String carType) {
        return dossierDefectRepository.countTotalDefects(centerId, startDate, endDate, carType);
    }
}