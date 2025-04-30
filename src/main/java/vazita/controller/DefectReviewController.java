package vazita.controller;

import vazita.dto.DefectFormDto;
import vazita.dto.DefectReportDto;
import vazita.service.DefectFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class DefectReviewController {
    private final DefectFormService defectReviewService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<Page<DefectReportDto>> getDefectReports(
            @RequestParam(required = false) String chassisNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(required = false) String inspectorId,
            Pageable pageable) {
        log.info("Fetching defect reports with filters");
        return ResponseEntity.ok(defectReviewService.getDefectReports(
                chassisNumber, fromDate, toDate, inspectorId, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<DefectReportDto> getDefectReportById(@PathVariable Long id) {
        log.info("Fetching defect report with id: {}", id);
        return ResponseEntity.ok(defectReviewService.getDefectReportById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<DefectReportDto> updateDefectReport(
            @PathVariable Long id, @Valid @RequestBody DefectReportEditRequest request) {
        log.info("Updating defect report with id: {}", id);
        return ResponseEntity.ok(defectReviewService.updateDefectReport(id, request));
    }
}