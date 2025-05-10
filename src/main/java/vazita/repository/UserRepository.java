package vazita.repository;

import vazita.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByIdUser(String idUser);
    
    @Query("SELECT u FROM User u WHERE u.centerId = :centerId")
    List<User> findAllByIdCentre(@Param("centerId") Integer centerId);
    
    @Query("SELECT u FROM User u WHERE u.idUser = :idUser AND u.centerId = :centerId")
    Optional<User> findByIdUserAndIdCentre(@Param("idUser") String idUser, @Param("centerId") Integer centerId);
    
    @Query("SELECT u FROM User u WHERE (u.lastName LIKE %:searchTerm% OR u.firstName LIKE %:searchTerm%) AND u.centerId = :centerId")
    List<User> searchUsersByCentre(@Param("searchTerm") String searchTerm, @Param("centerId") Integer centerId);
    
    @Query("SELECT u FROM User u WHERE u.status = 'A' AND u.centerId = :centerId")
    List<User> findActiveUsersByCentre(@Param("centerId") Integer centerId);
    
    boolean existsByIdUserAndCenterId(String idUser, Integer centerId);
}
