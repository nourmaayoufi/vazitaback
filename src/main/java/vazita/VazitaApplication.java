package vazita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.example.model") // Adjust package as needed

@EnableJpaRepositories(basePackages = "vazita.repository") // Explicitly enabling repository scanning

public class VazitaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VazitaApplication.class, args);
	}

}
