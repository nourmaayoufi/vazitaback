package vazita.controller;

import vazita.dto.*;
import vazita.service.DefectFormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/defects")
@RequiredArgsConstructor
@Slf4j
public class DefectFormController {
    private final DefectFormService defectFormService;

    @GetMapping("/chapters")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR', 'ADJOINT')")
    public ResponseEntity<List<ChapterDto>> getAllChapters() {
        log.info("Fetching all chapters");
        return ResponseEntity.ok(defectFormService.getAllChapters());
    }

    @GetMapping("/chapters/{chapterId}/points")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR', 'ADJOINT')")
    public ResponseEntity<List<DefectPointDto>> getPointsByChapter(@PathVariable String chapterId) {
        log.info("Fetching points for chapter: {}", chapterId);
        return ResponseEntity.ok(defectFormService.getPointsByChapter(chapterId));
    }

    @GetMapping("/chapters/{chapterId}/points/{pointId}/alterations")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR', 'ADJOINT')")
    public ResponseEntity<List<AlterationDto>> getAlterationsByPoint(
            @PathVariable String chapterId, @PathVariable String pointId) {
        log.info("Fetching alterations for chapter: {} and point: {}", chapterId, pointId);
        return ResponseEntity.ok(defectFormService.getAlterationsByPoint(chapterId, pointId));
    }

    @PostMapping("/submit")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<?> submitDefectForm(@Valid @RequestBody DefectFormDto submission) {
        log.info("Submitting defect form for dossier: {}", submission.getDossierNumber());
        defectFormService.submitDefectForm(submission);
        return ResponseEntity.ok().build();
    }
}
