package vazita.service;

import com.inspection.exception.ResourceNotFoundException;
import com.inspection.model.dto.*;
import com.inspection.model.entity.*;
import com.inspection.repository.*;
import com.inspection.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefectFormService {
    private final ChapterRepository chapterRepository;
    private final DefectPointRepository defectPointRepository;
    private final AlterationRepository alterationRepository;
    private final DefectReportRepository defectReportRepository;
    private final QueueService queueService;

    @Cacheable("chapters")
    @Transactional(readOnly = true)
    public List<ChapterDto> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();
        return chapters.stream().map(this::convertToChapterDto).collect(Collectors.toList());
    }

    @Cacheable(value = "points", key = "#chapterId")
    @Transactional(readOnly = true)
    public List<DefectPointDto> getPointsByChapter(String chapterId) {
        List<DefectPoint> points = defectPointRepository.findByChapterCode(chapterId);
        return points.stream().map(this::convertToPointDto).collect(Collectors.toList());
    }

    @Cacheable(value = "alterations", key = "#chapterId + '-' + #pointId")
    @Transactional(readOnly = true)
    public List<AlterationDto> getAlterationsByPoint(String chapterId, String pointId) {
        List<Alteration> alterations = alterationRepository.findByChapterCodeAndPointCode(chapterId, pointId);
        return alterations.stream().map(this::convertToAlterationDto).collect(Collectors.toList());
    }

    @Transactional
    public void submitDefectForm(DefectFormSubmissionDto submission) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        
        // Create defect reports for all selected defects
        for (DefectItemDto defect : submission.getDefects()) {
            DefectReport report = new DefectReport();
            report.setInspectionDate(new Date());
            report.setCenterId(userDetails.getCenterId());
            report.setDossierNumber(submission.getDossierNumber());
            report.setRegistrationTime(submission.getRegistrationTime());
            report.setChassisNumber(submission.getChassisNumber());
            report.setDefectCode(defect.getAlterationCode());
            report.setInspectorId(userDetails.getUsername());
            
            defectReportRepository.save(report);
        }
        
        // Remove the car from the queue
        queueService.removeFromQueue(submission.getDossierNumber());
        
        log.info("Defect form submitted for dossier: {}", submission.getDossierNumber());
    }

    private ChapterDto convertToChapterDto(Chapter chapter) {
        ChapterDto dto = new ChapterDto();
        dto.setCode(chapter.getCode());
        dto.setLabel(chapter.getLabel());
        return dto;
    }

    private DefectPointDto convertToPointDto(DefectPoint point) {
        DefectPointDto dto = new DefectPointDto();
        dto.setCode(point.getCode());
        dto.setLabel(point.getLabel());
        dto.setChapterCode(point.getChapterCode());
        return dto;
    }

    private AlterationDto convertToAlterationDto(Alteration alteration) {
        AlterationDto dto = new AlterationDto();
        dto.setCode(alteration.getCode());
        dto.setLabel(alteration.getLabel());
        dto.setChapterCode(alteration.getChapterCode());
        dto.setPointCode(alteration.getPointCode());
        return dto;
    }
}
