package vazita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Application class for the Vehicle Inspection System.
 * Sets up Spring Boot application with necessary configurations.
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableTransactionManagement
@EnableJpaRepositories
public class VazitaApplication {

    /**
     * Entry point for the Spring Boot application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(VazitaApplication.class, args);
    }
}