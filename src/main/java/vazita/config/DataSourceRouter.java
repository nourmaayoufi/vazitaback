package vazita.config;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DataSourceRouter extends AbstractRoutingDataSource {
    private static final ThreadLocal<String> currentCenter = new ThreadLocal<>();
    
    @Override
    protected Object determineCurrentLookupKey() {
        return getCurrentCenter();
    }
    
    public static void setCurrentCenter(String centerId) {
        currentCenter.set(centerId);
    }
    
    public static String getCurrentCenter() {
        return currentCenter.get() != null ? currentCenter.get() : "CENTRALE_MOBILE";
    }
    
    public static void clear() {
        currentCenter.remove();
    }
}
