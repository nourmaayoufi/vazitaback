package vazita.repository;
import vazita.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
/**
 * Repository for DossierDefaut entity (defect reports)
 */
@Repository
public interface DossierDefautRepository extends JpaRepository<DossierDefaut, Long> {
    List<DossierDefaut> findByNDossier(String nDossier);
    
    Page<DossierDefaut> findByNumCentreOrderByDateHeureEnregistrementDesc(String numCentre, Pageable pageable);
    
    @Query("SELECT dd FROM DossierDefaut dd WHERE dd.numCentre = :numCentre AND " +
           "(dd.numChassis LIKE %:keyword% OR dd.nDossier LIKE %:keyword% OR dd.matAgent LIKE %:keyword%)")
    Page<DossierDefaut> searchByKeywordAndCenter(@Param("keyword") String keyword, 
                                             @Param("numCentre") String numCentre, 
                                             Pageable pageable);
    
    @Query("SELECT dd FROM DossierDefaut dd WHERE dd.numCentre = :numCentre AND " +
           "dd.datCtrl BETWEEN :startDate AND :endDate")
    Page<DossierDefaut> findByDateRangeAndCenter(@Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate,
                                            @Param("numCentre") String numCentre,
                                            Pageable pageable);
    
    @Query(value = "SELECT cd.CODE_DEFAUT, COUNT(*) as count " +
                  "FROM DOSSIER_DEFAUTS cd " +
                  "WHERE cd.NUM_CENTRE = :centerId " +
                  "AND cd.DAT_CTRL BETWEEN :startDate AND :endDate " +
                  "GROUP BY cd.CODE_DEFAUT " +
                  "ORDER BY count DESC", 
          nativeQuery = true)
    List<Object[]> findMostFrequentDefectsByCenter(@Param("centerId") String centerId,
                                              @Param("startDate") Date startDate,
                                              @Param("endDate") Date endDate);
}