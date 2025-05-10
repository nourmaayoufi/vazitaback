package vazita.service;

import vazita.dto.inspection.CarDossierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InspectionService {
    Page<CarDossierDto> getCarQueue(Integer centerId, Pageable pageable);
    CarDossierDto getCarDossierById(Integer centerId, String dossierId);
    void removeCarFromQueue(Integer centerId, String dossierId);
    Page<CarDossierDto> searchCarQueue(Integer centerId, String searchTerm, Pageable pageable);
    Optional<CarDossierDto> findCarDossierByChassisNumber(Integer centerId, String chassisNumber);
    Optional<CarDossierDto> findCarDossierByLicensePlate(Integer centerId, String licensePlate);
}
