package vazita.util;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom routing data source for switching between center-specific databases.
 * Uses thread-local context to determine the current center.
 */
@Slf4j
public class DataSourceRouter extends AbstractRoutingDataSource {

    // Cache for DataSource instances to avoid recreating them
    private final Map<String, DataSource> centerDataSources = new ConcurrentHashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        // Get the center ID from thread-local context
        String centerId = CenterContextHolder.getCenterId();
        log.debug("Routing to data source for center: {}", centerId);
        return centerId != null ? centerId : "CENTRALE";
    }

    /**
     * Adds a new center data source dynamically
     * @param centerId Center ID
     * @param machine Database server hostname
     * @param sid Oracle SID
     * @param username Database username
     * @param password Database password
     * @return Created DataSource instance
     */
    public synchronized DataSource addCenterDataSource(String centerId, String machine, String sid, 
                                                    String username, String password) {
        if (centerDataSources.containsKey(centerId)) {
            log.debug("Using cached data source for center: {}", centerId);
            return centerDataSources.get(centerId);
        }

        log.info("Creating new data source for center: {}", centerId);
        
        HikariConfig config = new HikariConfig();
        String jdbcUrl = String.format("jdbc:oracle:thin:@%s:1521:%s", machine, sid);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        
        DataSource dataSource = new HikariDataSource(config);
        
        // Add to the target data sources
        Map<Object, Object> targetDataSources = new HashMap<>(getResolvedDataSources());
        targetDataSources.put(centerId, dataSource);
        setTargetDataSources(targetDataSources);
        afterPropertiesSet(); // Reinitialize the resolved data sources
        
        // Cache the data source
        centerDataSources.put(centerId, dataSource);
        
        return dataSource;
    }
}
