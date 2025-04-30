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
 * Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByIdUser(String idUser);
    
    List<User> findByIdCentre(String idCentre);
    
    Page<User> findByIdCentre(String idCentre, Pageable pageable);
    
    boolean existsByIdUserAndIdCentre(String idUser, String idCentre);
}