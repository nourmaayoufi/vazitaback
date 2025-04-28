package vazita.config;

// Class to manage tenant (center) context for multi-database routing
public class TenantContext {

    // ThreadLocal variable to hold the tenant ID separately for each thread (user request)
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    // Get the current tenant ID
    public static String getCurrentTenant() {
        // If no tenant is set, default to "CENTRAL"
        return CURRENT_TENANT.get() != null ? CURRENT_TENANT.get() : "CENTRAL";
    }

    // Set the tenant ID (called when user logs in or tenant is determined)
    public static void setCurrentTenant(String tenantId) {
        CURRENT_TENANT.set(tenantId);
    }

    // Clear the tenant ID (usually called at the end of the request to avoid memory leaks)
    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
