package vazita.util;

import org.springframework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * ThreadLocal storage for the current database center context
 * Used by the DataSourceRouter to determine which database to route to
 */
@Slf4j
public class CenterContextHolder {

    public static final String DEFAULT_CENTER = "CENTRALE";
    
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    /**
     * Set the current center context
     * @param center The center identifier
     */
    public static void setCenter(String center) {
        if (!StringUtils.hasText(center)) {
            throw new IllegalArgumentException("Center identifier cannot be empty");
        }
        log.debug("Setting center context to: {}", center);
        contextHolder.set(center);
    }

    /**
     * Get the current center context
     * @return The current center identifier or DEFAULT_CENTER if not set
     */
    public static String getCenter() {
        String center = contextHolder.get();
        if (!StringUtils.hasText(center)) {
            log.debug("No center context found, using default: {}", DEFAULT_CENTER);
            return DEFAULT_CENTER;
        }
        return center;
    }

    /**
     * Clear the current center context
     */
    public static void clearCenter() {
        log.debug("Clearing center context");
        contextHolder.remove();
    }
}