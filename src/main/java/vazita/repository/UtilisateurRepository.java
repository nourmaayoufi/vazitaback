package vazita.repository;

import org.springframework.data.jpa.repository.JpaRepository;  // Interface from Spring Data JPA to provide CRUD operations
import org.springframework.data.jpa.repository.Query;  // To define custom JPQL queries
import org.springframework.data.repository.query.Param;  // To bind query parameters to method arguments

import vazita.model.entity.Utilisateur;

import java.util.List;                         // For returning a list of Utilisateur entities
import java.util.Optional;                    // For returning an Optional, useful for handling nulls safely

// This interface defines a repository for the Utilisateur entity
// It extends JpaRepository, providing basic CRUD operations and custom query methods
public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {

    // Finds a Utilisateur by their idUser (assuming idUser is of type String)
    Optional<Utilisateur> findByIdUser(String idUser);
    
    // Custom query to find all Utilisateur entities by their center ID (idCentre)
    // Uses JPQL (Java Persistence Query Language) to fetch all users for a given center
    @Query("SELECT u FROM Utilisateur u WHERE u.idCentre = :centerId")
    List<Utilisateur> findAllByIdCentre(@Param("centerId") Integer centerId);
    
    // Custom query to check if a user exists with a specific idUser and password (passe)
    // Returns true if the user with the given idUser exists and the password matches
    @Query("SELECT COUNT(u) > 0 FROM Utilisateur u WHERE u.idUser = :idUser AND u.passe = :passe")
    boolean existsByIdUserAndPasse(@Param("idUser") String idUser, @Param("passe") String passe);
    
    // Finds if a user exists with the given idUser and idCentre (combination of user ID and center ID)
    boolean existsByIdUserAndIdCentre(String idUser, Integer idCentre);
}
