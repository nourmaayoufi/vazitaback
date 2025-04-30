package vazita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application class for the Vehicle Inspection App, bootstrapping the Spring Boot application.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"vazita.repository"})
public class VazitaApplication {

    /**
     * Entry point for the Spring Boot application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(VazitaApplication.class, args);
    }
}