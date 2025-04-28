package vazita.config;

// Import logging and Spring AbstractRoutingDataSource
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

// Lombok: Automatically add an SLF4J logger
@Slf4j
// Custom DataSource router for multi-tenant database routing
public class DataSourceRouter extends AbstractRoutingDataSource {

    // This method decides which database (DataSource) to use at runtime
    @Override
    protected Object determineCurrentLookupKey() {
        // Get the current tenant (center ID or database key) from a ThreadLocal context
        String tenantId = TenantContext.getCurrentTenant();
        
        // Log the currently selected tenant for debugging purposes
        log.debug("Current tenant ID: {}", tenantId);
        
        // Return the tenantId, which will be used to select the right DataSource
        return tenantId;
    }
}
