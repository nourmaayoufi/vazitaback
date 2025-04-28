package vazita.repository;

import org.springframework.data.jpa.repository.JpaRepository;  // Interface from Spring Data JPA to provide CRUD operations

import vazita.model.entity.CentreCVT;

// This interface defines a repository for the CentreCVT entity
// It extends JpaRepository, which provides various methods to interact with the database
public interface CentreCVTRepository extends JpaRepository<CentreCVT, Integer> {
    // No custom methods are needed here as JpaRepository provides the basic CRUD operations out of the box
    // You can add custom query methods if needed, like findBy... or findAllBy...
}
