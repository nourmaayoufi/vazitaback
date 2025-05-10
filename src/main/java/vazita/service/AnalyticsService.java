package vazita.service;

import vazita.dto.analytics.AnalyticsRequest;
import vazita.dto.analytics.AnalyticsResponse;

public interface AnalyticsService {
    AnalyticsResponse getDefectFrequency(AnalyticsRequest request);
    AnalyticsResponse getDefectsByCarType(AnalyticsRequest request);
    AnalyticsResponse getDefectTrends(AnalyticsRequest request);
    AnalyticsResponse getInspectorPerformance(AnalyticsRequest request);
    AnalyticsResponse getCustomAnalytics(AnalyticsRequest request);
}
