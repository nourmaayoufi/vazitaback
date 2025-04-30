package vazita.service;

import com.inspection.model.dto.CarQueueDto;
import com.inspection.model.entity.CarQueue;
import com.inspection.repository.CarQueueRepository;
import com.inspection.util.CenterContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {
    private final CarQueueRepository carQueueRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "carQueue", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #root.methodName")
    public Page<CarQueueDto> getCarQueue(Pageable pageable) {
        Page<CarQueue> queue = carQueueRepository.findAll(pageable);
        return queue.map(this::convertToDto);
    }

    @Transactional(readOnly = true)
    public Page<CarQueueDto> searchCarQueue(String chassisNumber, String registrationNumber, Pageable pageable) {
        Page<CarQueue> queue;
        
        if (chassisNumber != null && !chassisNumber.isEmpty()) {
            queue = carQueueRepository.findByChassisNumberContainingIgnoreCase(chassisNumber, pageable);
        } else if (registrationNumber != null && !registrationNumber.isEmpty()) {
            queue = carQueueRepository.findByRegistrationNumberContainingIgnoreCase(registrationNumber, pageable);
        } else {
            queue = carQueueRepository.findAll(pageable);
        }
        
        return queue.map(this::convertToDto);
    }

    @Transactional
    public void removeFromQueue(String dossierNumber) {
        carQueueRepository.deleteByDossierNumber(dossierNumber);
        log.info("Removed car with dossier number {} from queue", dossierNumber);
    }

    private CarQueueDto convertToDto(CarQueue carQueue) {
        CarQueueDto dto = new CarQueueDto();
        dto.setDossierNumber(carQueue.getDossierNumber());
        dto.setChassisNumber(carQueue.getChassisNumber());
        dto.setRegistrationNumber(carQueue.getRegistrationNumber());
        dto.setLaneCode(carQueue.getLaneCode());
        dto.setRegistrationTime(carQueue.getRegistrationTime());
        return dto;
    }
}