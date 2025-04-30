package vazita.controller;

import vazita.dto.QueueItemDto;
import vazita.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queue")
@RequiredArgsConstructor
@Slf4j
public class QueueController {
    private final QueueService queueService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR', 'ADJOINT')")
    public ResponseEntity<Page<QueueItemDto>> getQueue(Pageable pageable) {
        log.info("Fetching car queue");
        return ResponseEntity.ok(queueService.getCarQueue(pageable));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'INSPECTOR', 'ADJOINT')")
    public ResponseEntity<Page<QueueItemDto>> searchQueue(
            @RequestParam(required = false) String chassisNumber,
            @RequestParam(required = false) String registrationNumber,
            Pageable pageable) {
        log.info("Searching car queue with filters");
        return ResponseEntity.ok(queueService.searchCarQueue(chassisNumber, registrationNumber, pageable));
    }
}
