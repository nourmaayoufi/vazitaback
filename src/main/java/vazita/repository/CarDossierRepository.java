package vazita.repository;

import vazita.entity.CarDossier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarDossierRepository extends JpaRepository<CarDossier, String> {

    Page<CarDossier> findAllByOrderByDateHeureEnregistrementDesc(Pageable pageable);

    Optional<CarDossier> findByNDossier(String nDossier);

    Optional<CarDossier> findByNumChassis(String numChassis);

    Optional<CarDossier> findByImmatriculation(String immatriculation);

    @Query("SELECT c FROM CarDossier c WHERE c.immatriculation LIKE %:searchTerm% OR c.numChassis LIKE %:searchTerm%")
    Page<CarDossier> searchCars(@Param("searchTerm") String searchTerm, Pageable pageable);
}
