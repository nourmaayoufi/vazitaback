package vazita.util;

/**
 * Thread-local storage for current datasource identifier.
 * Used by CenterRoutingDataSource to determine the correct center database.
 */
public class DataSourceContextHolder {
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public static final String CENTRAL_DATASOURCE = "CENTRAL";

    public static void setDataSourceId(String dataSourceId) {
        contextHolder.set(dataSourceId);
    }

    public static String getDataSourceId() {
        return contextHolder.get();
    }

    public static void clear() {
        contextHolder.remove();
    }

    public static void setCentralContext() {
        setDataSourceId(CENTRAL_DATASOURCE);
    }

    public static void setCenterId(Integer centerId) {
        if (centerId != null) {
            setDataSourceId("MOBILE_" + centerId);
        }
    }
}
