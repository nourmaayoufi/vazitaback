package vazita.repository;

import vazita.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {
    
    Optional<Chapter> findByChapterCode(Integer chapterCode);

    List<Chapter> findAllByOrderByChapterCodeAsc();
}
