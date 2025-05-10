package vazita.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vazita.entity.Group;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {
    
    Optional<Group> findByCodGrp(Integer codGrp);
    
}