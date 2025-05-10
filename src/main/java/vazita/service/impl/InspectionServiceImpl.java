package vazita.service.impl;

import vazita.dto.inspection.CarDossierDto;
import vazita.entity.CarDossier;
import vazita.exception.ResourceNotFoundException;
import vazita.repository.CarDossierRepository;
import vazita.service.InspectionService;
import vazita.util.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InspectionServiceImpl implements InspectionService {

    private final CarDossierRepository carDossierRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CarDossierDto> getCarQueue(Integer centerId, Pageable pageable) {
        DataSourceContextHolder.setCenterId(centerId);
        Page<CarDossier> carQueuePage = carDossierRepository.findAllByOrderByDateHeureEnregistrementDesc(pageable);
        return carQueuePage.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CarDossierDto> searchCarQueue(Integer centerId, String searchTerm, Pageable pageable) {
        DataSourceContextHolder.setCenterId(centerId);
        Page<CarDossier> carQueuePage = (searchTerm != null && !searchTerm.isEmpty())
                ? carDossierRepository.searchCars(searchTerm, pageable)
                : carDossierRepository.findAllByOrderByDateHeureEnregistrementDesc(pageable);
        return carQueuePage.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CarDossierDto getCarDossierById(Integer centerId, String dossierId) {
        DataSourceContextHolder.setCenterId(centerId);
        CarDossier carDossier = carDossierRepository.findById(dossierId)
                .orElseThrow(() -> new ResourceNotFoundException("Car dossier not found"));
        return convertToDto(carDossier);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarDossierDto> findCarDossierByChassisNumber(Integer centerId, String chassisNumber) {
        DataSourceContextHolder.setCenterId(centerId);
        return carDossierRepository.findByNumChassis(chassisNumber).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CarDossierDto> findCarDossierByLicensePlate(Integer centerId, String licensePlate) {
        DataSourceContextHolder.setCenterId(centerId);
        return carDossierRepository.findByImmatriculation(licensePlate).map(this::convertToDto);
    }

    @Override
    @Transactional
    public void removeCarFromQueue(Integer centerId, String dossierId) {
        DataSourceContextHolder.setCenterId(centerId);
        if (!carDossierRepository.existsById(dossierId)) {
            throw new ResourceNotFoundException("Car dossier not found");
        }
        carDossierRepository.deleteById(dossierId);
    }

    private CarDossierDto convertToDto(CarDossier carDossier) {
        return CarDossierDto.builder()
                .nDossier(carDossier.getDossierNumber())
                .numChassis(carDossier.getChassisNumber())
                .immatriculation(carDossier.getRegistrationNumber())
                .cPiste(carDossier.getTrackCode())
                .dateHeureEnregistrement(carDossier.getRegistrationTime() == null ? null :
                        carDossier.getRegistrationTime().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime())
                .build();
    }


}
