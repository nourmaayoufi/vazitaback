package vazita.repository;

import vazita.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CenterRepository extends JpaRepository<Center, Integer> {
    
    // Find a center by its unique ID (idCentre)
    Optional<Center> findByIdCentre(Integer idCentre);
}
