package vazita.controller;


import vazita.dto.inspection.*;
import vazita.service.DefectFormService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * Controller for defect form operations
 */
@RestController
@RequestMapping("/api/defects")
@RequiredArgsConstructor
public class DefectController {

    private final DefectFormService defectFormService;

    /**
     * Get all chapters for defect form
     *
     * @return list of chapters
     */
    @GetMapping("/chapters")
    @PreAuthorize("hasAnyRole('INSPECTOR', 'ADMIN', 'ADJOINT')")
    public ResponseEntity<List<ChapterDto>> getAllChapters() {
        return ResponseEntity.ok(defectFormService.getAllChapters());
    }

    /**
     * Get defect points by chapter
     *
     * @param chapterCode chapter code
     * @return list of defect points for the chapter
     */
    @GetMapping("/points/{chapterCode}")
    @PreAuthorize("hasAnyRole('INSPECTOR', 'ADMIN', 'ADJOINT')")
    public ResponseEntity<List<DefectPointDto>> getPointsByChapter(@PathVariable String chapterCode) {
        return ResponseEntity.ok(defectFormService.getPointsByChapter(chapterCode));
    }

    /**
     * Get alterations by chapter and point
     *
     * @param chapterCode chapter code
     * @param pointCode point code
     * @return list of alterations
     */
    @GetMapping("/alterations/{chapterCode}/{pointCode}")
    @PreAuthorize("hasAnyRole('INSPECTOR', 'ADMIN', 'ADJOINT')")
    public ResponseEntity<List<AlterationDto>> getAlterationsByChapterAndPoint(
            @PathVariable String chapterCode,
            @PathVariable String pointCode) {
        return ResponseEntity.ok(defectFormService.getAlterationsByPoint(chapterCode, pointCode));
    }

    /**
     * Submit defect form
     *
     * @param request defect submission request
     * @return success response
     */
    @PostMapping("/submit")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<String> submitDefectForm(@Valid @RequestBody DefectSubmissionRequest request) {
        defectFormService.submitDefectForm(request);
        return new ResponseEntity<>("Defect form submitted successfully", HttpStatus.CREATED);
    }

    /**
     * Get defect reports with pagination and filtering
     *
     * @param startDate optional start date filter
     * @param endDate optional end date filter
     * @param inspectorId optional inspector ID filter
     * @param chassisNumber optional chassis number filter
     * @param pageable pagination parameters
     * @return page of defect reports
     */
    @GetMapping("/reports")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<Page<CarDossierDto>> getDefectReports(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) Long inspectorId,
            @RequestParam(required = false) String chassisNumber,
            Pageable pageable) {
        return ResponseEntity.ok(defectFormService.getDefectReports(
                startDate, endDate, inspectorId, chassisNumber, pageable));
    }

    /**
     * Get defect report by ID
     *
     * @param reportId report ID
     * @return defect report details
     */
    @GetMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<DefectSubmissionRequest> getDefectReportById(@PathVariable Long reportId) {
        return ResponseEntity.ok(defectFormService.getDefectReportById(reportId));
    }

    /**
     * Update defect report
     *
     * @param reportId report ID
     * @param defect updated defect data
     * @return updated defect report
     */
    @PutMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<DossierDefectDto> updateDefectReport(
            @PathVariable Long reportId,
            @Valid @RequestBody DossierDefectDto defect) {
        return ResponseEntity.ok(defectFormService.updateDefectReport(reportId, defect));
    }

    /**
     * Delete defect report
     *
     * @param reportId report ID
     * @return success message
     */
    @DeleteMapping("/reports/{reportId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    public ResponseEntity<String> deleteDefectReport(@PathVariable Long reportId) {
        defectFormService.deleteDefectReport(reportId);
        return ResponseEntity.ok("Defect report deleted successfully");
    }
}
