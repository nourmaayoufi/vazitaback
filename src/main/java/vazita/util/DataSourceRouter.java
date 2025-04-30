package vazita.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Custom implementation of AbstractRoutingDataSource that routes to the correct 
 * center database based on the CenterContextHolder
 */
@Slf4j
public class DataSourceRouter extends AbstractRoutingDataSource {
    
    private final Map<Object, Object> dynamicDataSources = new ConcurrentHashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return CenterContextHolder.getCenter();
    }
    
    /**
     * Adds a new center datasource dynamically
     * 
     * @param centerKey The center identifier
     * @param jdbcUrl The JDBC URL for the center's database
     * @param username Database username
     * @param password Database password
     * @return true if the datasource was added successfully, false otherwise
     */
    public synchronized boolean addCenterDataSource(String centerKey, String jdbcUrl, String username, String password) {
        try {
            if (dynamicDataSources.containsKey(centerKey)) {
                log.info("Data source for center {} already exists", centerKey);
                return true;
            }
            
            log.info("Adding new data source for center: {}", centerKey);
            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(20000);
            config.setPoolName("HikariPool-" + centerKey);
            
            DataSource dataSource = new HikariDataSource(config);
            
            // Add to our local tracking map
            dynamicDataSources.put(centerKey, dataSource);
            
            // Update the target datasources
            Map<Object, Object> targetDataSources = new ConcurrentHashMap<>(dynamicDataSources);
            setTargetDataSources(targetDataSources);
            
            // Reinitialize the router with the new data source
            afterPropertiesSet();
            
            log.info("Data source for center {} added successfully", centerKey);
            return true;
        } catch (Exception e) {
            log.error("Failed to add data source for center: " + centerKey, e);
            return false;
        }