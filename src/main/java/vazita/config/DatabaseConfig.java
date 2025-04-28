package vazita.config;

// Import necessary libraries for database, entity manager, transactions, logging, etc.
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// Mark this class as a configuration class for Spring Boot
@Configuration
// Enable JPA repositories in the specified package, and link to custom EntityManagerFactory and TransactionManager
@EnableJpaRepositories(
        basePackages = "com.vehicleinspection.repository",
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager"
)
// Lombok: Automatically generate constructor for required fields
@RequiredArgsConstructor
// Lombok: Enable logging (using slf4j)
@Slf4j
public class DatabaseConfig {

    // Read properties from application.yml or application.properties
    @Value("${app.central-datasource.url}")
    private String centralDbUrl;
    
    @Value("${app.central-datasource.username}")
    private String centralDbUsername;
    
    @Value("${app.central-datasource.password}")
    private String centralDbPassword;
    
    // Optional HikariCP configuration properties with default values
    @Value("${app.central-datasource.hikari.minimum-idle:5}")
    private int minimumIdle;
    
    @Value("${app.central-datasource.hikari.maximum-pool-size:20}")
    private int maximumPoolSize;
    
    @Value("${app.central-datasource.hikari.idle-timeout:30000}")
    private long idleTimeout;
    
    @Value("${app.central-datasource.hikari.max-lifetime:2000000}")
    private long maxLifetime;
    
    @Value("${app.central-datasource.hikari.connection-timeout:30000}")
    private long connectionTimeout;

    // Define the central database DataSource bean (main database connection)
    @Bean
    @Primary  // Mark this as the default DataSource if multiple exist
    public DataSource centralDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(centralDbUrl);  // Central database URL
        config.setUsername(centralDbUsername);
        config.setPassword(centralDbPassword);
        config.setDriverClassName("oracle.jdbc.OracleDriver");  // Oracle driver
        config.setMinimumIdle(minimumIdle);
        config.setMaximumPoolSize(maximumPoolSize);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        config.setPoolName("CentralHikariPool");  // Naming the pool
        
        return new HikariDataSource(config);  // Create and return HikariCP DataSource
    }

    // Define a DataSource router bean (to allow multi-database routing if needed)
    @Bean
    public DataSourceRouter dataSourceRouter(@Qualifier("centralDataSource") DataSource centralDataSource) {
        DataSourceRouter router = new DataSourceRouter();
        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("CENTRAL", centralDataSource);  // Register the central database
        router.setTargetDataSources(dataSources);  // Set available data sources
        router.setDefaultTargetDataSource(centralDataSource);  // Default fallback
        return router;
    }

    // Define EntityManagerFactory (responsible for managing entities and database sessions)
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSourceRouter dataSourceRouter) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSourceRouter);  // Use the DataSourceRouter
        
        // Scan entity classes in this package
        em.setPackagesToScan("com.vehicleinspection.model.entity");
        
        // Set Hibernate as the JPA vendor
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        // Set Hibernate properties
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.OracleDialect");  // Oracle DB
        properties.put("hibernate.show_sql", "true");  // Print SQL to console (for debugging)
        properties.put("hibernate.format_sql", "true");  // Pretty print SQL
        properties.put("hibernate.multiTenancy", "DATABASE");  // Enable database-based multitenancy
        properties.put("hibernate.tenant_identifier_resolver", "com.vehicleinspection.config.CurrentTenantIdentifierResolverImpl"); 
        // Custom class to dynamically choose which tenant (database) to use
        em.setJpaProperties(properties);
        
        return em;
    }

    // Define the PlatformTransactionManager bean (handles transactions)
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);  // Link it to our EntityManagerFactory
        return transactionManager;
    }
}
