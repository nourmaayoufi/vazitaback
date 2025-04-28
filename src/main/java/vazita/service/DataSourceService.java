package vazita.service;


import vazita.config.DataSourceRouter;
import vazita.model.entity.CentreCVT;
import vazita.repository.CentreCVTRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataSourceService {
    
    private final CentreCVTRepository centreCVTRepository;  // Repository to fetch center configurations
    private final DataSourceRouter dataSourceRouter;        // Custom data source router for multi-tenancy support
    
    // Method to load data sources for all centers
    @PostConstruct
    public void loadDataSources() {
        log.info("Loading center-specific data sources");
        
        // Fetch all centers from the repository
        List<CentreCVT> centers = centreCVTRepository.findAll();
        
        Map<Object, Object> dataSources = new HashMap<>();
        // Add the default "CENTRAL" data source
        dataSources.put("CENTRAL", dataSourceRouter.getResolvedDefaultDataSource());
        
        // Loop through all centers and create their data sources
        for (CentreCVT center : centers) {
            try {
                DataSource centerDs = createCenterDataSource(center);
                dataSources.put(center.getIdCentre().toString(), centerDs);
                log.info("Added data source for center: {}", center.getIdCentre());
            } catch (Exception e) {
                log.error("Failed to create data source for center: {}", center.getIdCentre(), e);
            }
        }
        
        // Set all the data sources in the router
        dataSourceRouter.setTargetDataSources(dataSources);
        dataSourceRouter.afterPropertiesSet();
        
        log.info("Finished loading {} center data sources", centers.size());
    }
    
    // Helper method to create a data source for each center
    private DataSource createCenterDataSource(CentreCVT center) {
        String url = String.format("jdbc:oracle:thin:@%s:1521:%s", 
                center.getMachine(), center.getSid());  // Build the JDBC URL for the center
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);  // Set JDBC URL
        config.setUsername(center.getUsername());  // Set username for DB connection
        config.setPassword(center.getPassword());  // Set password for DB connection
        config.setDriverClassName("oracle.jdbc.OracleDriver");  // Set Oracle driver
        config.setMinimumIdle(3);  // Minimum number of idle connections
        config.setMaximumPoolSize(10);  // Max number of connections in the pool
        config.setIdleTimeout(30000);  // Idle timeout in ms
        config.setMaxLifetime(2000000);  // Max lifetime of a connection in ms
        config.setConnectionTimeout(30000);  // Max wait time for a connection
        config.setPoolName("Center" + center.getIdCentre() + "HikariPool");  // Set pool name
        
        return new HikariDataSource(config);  // Return the configured data source
    }
    
    // Method to dynamically register a new center's data source
    public void registerNewCenterDataSource(CentreCVT center) {
        try {
            DataSource centerDs = createCenterDataSource(center);  // Create new data source
            
            Map<Object, Object> targetDataSources = new HashMap<>();
            targetDataSources.put(center.getIdCentre().toString(), centerDs);  // Add to data sources map
            
            // Set the new data sources in the router
            dataSourceRouter.setTargetDataSources(targetDataSources);
            dataSourceRouter.afterPropertiesSet();  // Re-initialize the data source router
            
            log.info("Added new data source for center: {}", center.getIdCentre());
        } catch (Exception e) {
            log.error("Failed to create new data source for center: {}", center.getIdCentre(), e);
            throw new RuntimeException("Failed to register new center data source", e);
        }
    }
}
