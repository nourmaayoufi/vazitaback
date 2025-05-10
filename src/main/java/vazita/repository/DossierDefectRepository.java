package vazita.repository;

import vazita.entity.DossierDefect;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DossierDefectRepository extends JpaRepository<DossierDefect, Long> {
    
    List<DossierDefect> findByNDossier(String nDossier);
    
    List<DossierDefect> findByNumChassis(String numChassis);
    
    Page<DossierDefect> findByMatAgent(String matAgent, Pageable pageable);
    
    Page<DossierDefect> findByDatCtrlBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    @Query("SELECT d FROM DossierDefect d WHERE d.numCentre = :centreId")
    Page<DossierDefect> findByCentre(@Param("centreId") String centreId, Pageable pageable);
    
    @Query("SELECT d FROM DossierDefect d WHERE d.codeDefaut LIKE :codePattern%")
    Page<DossierDefect> findByDefectCodePattern(@Param("codePattern") String codePattern, Pageable pageable);
    
    @Query("SELECT COUNT(d) FROM DossierDefect d WHERE d.codeDefaut LIKE :chapterCode% GROUP BY d.codeDefaut")
    List<Object[]> countDefectsByChapter(@Param("chapterCode") String chapterCode);
    
    @Query("SELECT d.codeDefaut, COUNT(d) FROM DossierDefect d WHERE d.datCtrl BETWEEN :startDate AND :endDate GROUP BY d.codeDefaut ORDER BY COUNT(d) DESC")
    List<Object[]> findTopDefectsInTimeRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}