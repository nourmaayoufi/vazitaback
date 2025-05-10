package vazita.service.impl;

import vazita.exception.ApiException;
import vazita.exception.ResourceNotFoundException;
import vazita.dto.inspection.*;
import vazita.entity.*;
import vazita.repository.*;
import vazita.service.DefectFormService;
import vazita.service.InspectionService;
import vazita.util.DataSourceContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import vazita.exception.ResourceNotFoundException;




@Service
@RequiredArgsConstructor
public class DefectFormServiceImpl implements DefectFormService {

    private final ChapterRepository chapterRepository;
    private final DefectPointRepository defectPointRepository;
    private final AlterationRepository alterationRepository;
    private final DossierDefectRepository dossierDefectRepository;
    private final CarDossierRepository carDossierRepository;

    @Override
    @PreAuthorize("hasRole('INSPECTOR')")
    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('INSPECTOR')")
    public List<DefectPoint> getPointsByChapter(String chapterCode) {
        return defectPointRepository.findByChapterCode(chapterCode);
    }

    @Override
    @PreAuthorize("hasRole('INSPECTOR')")
    public List<Alteration> getAlterationsByChapterAndPoint(String chapterCode, String pointCode) {
        return alterationRepository.findByChapterCodeAndPointCode(chapterCode, pointCode);
    }

    @Override
    @PreAuthorize("hasRole('INSPECTOR')")
    @Transactional
    public void submitDefectForm(DefectSubmissionRequest submissionRequest) {
        // Verify the car dossier exists
        carDossierRepository.findById(submissionRequest.getDossierNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Car dossier not found with id: " + submissionRequest.getDossierNumber()));

        // Process each defect in the form
        List<DossierDefect> defects = submissionRequest.getDefects().stream()
                .map(defectDto -> {
                    // Build the defect code (chifra) by concatenating chapter, point, and alteration codes
                    String defectCode = defectDto.getChapterCode() + defectDto.getPointCode() + defectDto.getAlterationCode();
                    
                    // Create and populate the DossierDefect entity
                    DossierDefect dossierDefect = new DossierDefect();
                    dossierDefect.setDatCtrl(LocalDateTime.now());
                    dossierDefect.setNumCentre(DataSourceContextHolder.getCurrentCenterId());
                    dossierDefect.setNDossier(submissionRequest.getDossierNumber());
                    dossierDefect.setDateHeureEnregistrement(LocalDateTime.now());
                    dossierDefect.setNumChassis(submissionRequest.getChassisNumber());
                    dossierDefect.setCodeDefaut(defectCode);
                    dossierDefect.setMatAgent(submissionRequest.getInspectorId());
                    
                    return dossierDefect;
                })
                .collect(Collectors.toList());

        // Save all defects in a batch
        dossierDefectRepository.saveAll(defects);
        
        // Remove the inspected car from the queue
        carDossierRepository.markAsProcessed(submissionRequest.getDossierNumber());
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT', 'INSPECTOR')")
    public DefectFormDto getDefectFormByDossier(String dossierNumber) {
        // Retrieve all defects for the given dossier
        List<DossierDefect> defects = dossierDefectRepository.findByNDossier(dossierNumber);
        
        if (defects.isEmpty()) {
            throw new ResourceNotFoundException("No defects found for dossier: " + dossierNumber);
        }
        
        // Build the form DTO
        DefectFormDto formDto = new DefectFormDto();
        formDto.setDossierNumber(dossierNumber);
        formDto.setChassisNumber(defects.get(0).getNumChassis());
        formDto.setInspectorId(defects.get(0).getMatAgent());
        formDto.setInspectionDate(defects.get(0).getDateHeureEnregistrement());
        
        // Map each defect to a DTO
        formDto.setDefects(defects.stream()
                .map(defect -> {
                    // Extract chapter, point, and alteration codes from the defect code
                    String defectCode = defect.getCodeDefaut();
                    String chapterCode = defectCode.substring(0, 1);
                    String pointCode = defectCode.substring(1, 2);
                    String alterationCode = defectCode.substring(2, 3);
                    
                    // Get the entities to include their labels in the DTO
                    Chapter chapter = chapterRepository.findById(chapterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Chapter not found: " + chapterCode));
                    
                    DefectPoint point = defectPointRepository.findByCodePointAndCodeChapitre(pointCode, chapterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Point not found: " + pointCode));
                    
                    Alteration alteration = alterationRepository.findByCodeAlterationAndCodeChapitreAndCodePoint(
                            alterationCode, chapterCode, pointCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Alteration not found: " + alterationCode));
                    
                    // Build the defect DTO
                    DefectFormDto.DefectDto defectDto = new DefectFormDto.DefectDto();
                    defectDto.setChapterCode(chapterCode);
                    defectDto.setChapterName(chapter.getLibelleChapitre());
                    defectDto.setPointCode(pointCode);
                    defectDto.setPointName(point.getLibellePoint());
                    defectDto.setAlterationCode(alterationCode);
                    defectDto.setAlterationName(alteration.getLibelleAlteration());
                    
                    return defectDto;
                })
                .collect(Collectors.toList()));
        
        return formDto;
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'ADJOINT')")
    @Transactional
    public void updateDefectForm(String dossierNumber, DefectFormDto updatedForm) {
        // Delete existing defects for this dossier
        dossierDefectRepository.deleteByNDossier(dossierNumber);
        
        // Create a submission request from the form data
        DefectSubmissionRequest submissionRequest = new DefectSubmissionRequest();
        submissionRequest.setDossierNumber(dossierNumber);
        submissionRequest.setChassisNumber(updatedForm.getChassisNumber());
        submissionRequest.setInspectorId(updatedForm.getInspectorId());
        submissionRequest.setDefects(updatedForm.getDefects());
        
        // Reuse the submission logic
        submitDefectForm(submissionRequest);
    }
}
