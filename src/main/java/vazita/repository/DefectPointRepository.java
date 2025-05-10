package vazita.repository;

import vazita.entity.DefectPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DefectPointRepository extends JpaRepository<DefectPoint, Integer> {
    
    List<DefectPoint> findByChapterCode(Integer chapterCode);

    Optional<DefectPoint> findByPointCodeAndChapterCode(Integer pointCode, Integer chapterCode);
}
