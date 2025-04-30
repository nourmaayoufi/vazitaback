package vazita.config;

import vazita.util.CenterContextHolder;
import vazita.util.DataSourceRouter;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for datasources including the central database and 
 * routing for center-specific databases
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSourceConfig {

    /**
     * Create the central data source for authentication and user management
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.centrale")
    public HikariConfig centralHikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource centralDataSource() {
        try {
            HikariDataSource dataSource = new HikariDataSource(centralHikariConfig());
            log.info("Central data source initialized successfully");
            return dataSource;
        } catch (Exception e) {
            log.error("Failed to initialize central data source", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Create the routing data source that switches between different center databases
     */
    @Bean
    @Primary
    public DataSource routingDataSource() {
        try {
            AbstractRoutingDataSource routingDataSource = new DataSourceRouter();
            
            // Set the central data source as default
            DataSource defaultDataSource = centralDataSource();
            routingDataSource.setDefaultTargetDataSource(defaultDataSource);
            
            // Initialize with central data source, centers will be added dynamically
            Map<Object, Object> dataSources = new HashMap<>();
            dataSources.put(CenterContextHolder.DEFAULT_CENTER, defaultDataSource);
            
            routingDataSource.setTargetDataSources(dataSources);
            routingDataSource.afterPropertiesSet();
            
            log.info("Routing data source initialized successfully");
            return routingDataSource;
        } catch (Exception e) {
            log.error("Failed to initialize routing data source", e);
            throw new RuntimeException("Database routing initialization failed", e);
        }
    }
}