package vazita.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Custom routing datasource that determines which center database to use
 * based on the current DataSourceContextHolder value
 */
public class CenterRoutingDataSource extends AbstractRoutingDataSource {
    
    private static final Logger logger = LoggerFactory.getLogger(CenterRoutingDataSource.class);
    
    /**
     * Determines the current lookup key for the datasource to use
     * Based on the center ID stored in the thread-local context
     * @return The center ID as the lookup key
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSourceId = DataSourceContextHolder.getDataSourceId();
        logger.debug("Routing to datasource: {}", dataSourceId);
        return dataSourceId;
    }
}