package vazita.config;

// Importing Hibernate's interface for resolving the current tenant dynamically
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * Implementation of CurrentTenantIdentifierResolver.
 * This class is responsible for telling Hibernate which tenant (schema/database)
 * should be used for the current session.
 */
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver<String> {

    /**
     * Determines the current tenant identifier.
     * Hibernate calls this method whenever it needs to know which tenant to connect to.
     * 
     * @return the tenant identifier (for example, the center's schema name)
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        // Fetching the current tenant from the TenantContext
        return TenantContext.getCurrentTenant();
    }

    /**
     * Validates whether the existing Hibernate session is still valid
     * even if the tenant identifier changes during the session.
     * 
     * @return true to allow continuing the existing session
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        // Returning true allows session reuse even if the tenant changes
        return true;
    }
}
