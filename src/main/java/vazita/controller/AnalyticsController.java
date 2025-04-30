package vazita.controller;

import vazita.dto.AnalyticsDto;
import vazita.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@Slf4j
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/defects/frequency")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<AnalyticsDto> getDefectFrequency(
            @RequestParam(required = false) String carType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        log.info("Fetching defect frequency analytics");
        return ResponseEntity.ok(analyticsService.getDefectFrequency(carType, fromDate, toDate));
    }

    @GetMapping("/defects/by-chapter")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<AnalyticsDto> getDefectsByChapter(
            @RequestParam(required = false) String carType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        log.info("Fetching defects by chapter analytics");
        return ResponseEntity.ok(analyticsService.getDefectsByChapter(carType, fromDate, toDate));
    }

    @GetMapping("/defects/trends")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<AnalyticsDto> getDefectTrends(
            @RequestParam(required = false) String carType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate) {
        log.info("Fetching defect trends analytics");
        return ResponseEntity.ok(analyticsService.getDefectTrends(carType, fromDate, toDate));
    }
}
