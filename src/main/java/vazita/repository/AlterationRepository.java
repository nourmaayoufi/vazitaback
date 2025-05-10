package vazita.repository;

import vazita.entity.Alteration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlterationRepository extends JpaRepository<Alteration, Integer> {

    // Fetch all alterations for a specific chapter and point
    List<Alteration> findByChapterCodeAndPointCode(Integer chapterCode, Integer pointCode);

    // Fetch a specific alteration under a given chapter and point
    Optional<Alteration> findByAlterationCodeAndChapterCodeAndPointCode(
            Integer alterationCode,
            Integer chapterCode,
            Integer pointCode
    );
}
