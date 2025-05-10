package vazita.controller;


import vazita.dto.inspection.CarDossierDto;
import vazita.service.InspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for inspection queue management
 */
@RestController
@RequestMapping("/api/inspections")
@RequiredArgsConstructor
public class InspectionController {

    private final InspectionService inspectionService;

    /**
     * Get all cars in queue with pagination
     *
     * @param pageable pagination parameters
     * @return page of car dossiers
     */
    @GetMapping("/queue")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<Page<CarDossierDto>> getCarQueue(Pageable pageable) {
        return ResponseEntity.ok(inspectionService.getCarQueue(pageable));
    }

    /**
     * Get car dossier by N_DOSSIER
     *
     * @param dossierNumber dossier number
     * @return car dossier details
     */
    @GetMapping("/queue/{dossierNumber}")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<CarDossierDto> getCarDossier(@PathVariable String dossierNumber) {
        return ResponseEntity.ok(inspectionService.getCarDossier(dossierNumber));
    }

    /**
     * Search car queue by chassis number or registration
     *
     * @param query search query (chassis number or registration)
     * @param pageable pagination parameters
     * @return page of matching car dossiers
     */
    @GetMapping("/queue/search")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<Page<CarDossierDto>> searchCarQueue(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(inspectionService.searchCarQueue(query, pageable));
    }

    /**
     * Refresh car queue (force reload from database)
     *
     * @param pageable pagination parameters
     * @return updated page of car dossiers
     */
    @PostMapping("/queue/refresh")
    @PreAuthorize("hasRole('INSPECTOR')")
    public ResponseEntity<Page<CarDossierDto>> refreshCarQueue(Pageable pageable) {
        return ResponseEntity.ok(inspectionService.refreshCarQueue(pageable));
    }

    /**
     * Remove car from queue (used for testing/admin purposes)
     *
     * @param dossierNumber dossier number
     * @return success message
     */
    @DeleteMapping("/queue/{dossierNumber}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeFromQueue(@PathVariable String dossierNumber) {
        inspectionService.removeFromQueue(dossierNumber);
        return ResponseEntity.ok("Car removed from queue successfully");
    }
}