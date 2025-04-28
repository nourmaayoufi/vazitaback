package vazita.repository;

import org.springframework.data.jpa.repository.JpaRepository;  // Interface from Spring Data JPA to provide CRUD operations

import vazita.model.entity.Groupe;

// This interface defines a repository for the Groupe entity
// It extends JpaRepository, which provides various methods to interact with the database
public interface GroupeRepository extends JpaRepository<Groupe, String> {
    // No custom methods are needed here as JpaRepository provides the basic CRUD operations out of the box
    // You can add custom query methods if needed, like findBy... or findAllBy...
}
