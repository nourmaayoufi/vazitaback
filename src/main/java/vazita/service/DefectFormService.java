package vazita.service;

import vazita.dto.inspection.*;
import java.util.List;

public interface DefectFormService {
    List<DossierDefectDto> saveDossierDefects(List<DossierDefectDto> defectDtos, String userRole);
    List<DossierDefectDto> getDossierDefectsByDossierId(String dossierId, String userRole);
    void deleteDossierDefectById(Long id, String userRole);
	DefectFormDto getDefectFormByDossier(String dossierNumber);
}
