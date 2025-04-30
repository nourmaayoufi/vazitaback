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
 * Repository for Dossier entity (car queue)
 */
@Repository
public interface DossierRepository extends JpaRepository<Dossier, String> {
    Page<Dossier> findAllByOrderByDateHeureEnregistrementDesc(Pageable pageable);
    
    @Query("SELECT d FROM Dossier d WHERE d.immatriculation LIKE %:keyword% OR d.numChassis LIKE %:keyword%")
    Page<Dossier> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
