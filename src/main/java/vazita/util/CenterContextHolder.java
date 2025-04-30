package vazita.util;


import lombok.extern.slf4j.Slf4j;

/**
 * Thread-local storage for center context to support multi-tenant database routing.
 */
@Slf4j
public class CenterContextHolder {
    private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

    /**
     * Set the current center ID for this thread
     * @param centerId ID of the center
     */
    public static void setCenterId(String centerId) {
        log.debug("Setting center context: {}", centerId);
        CONTEXT.set(centerId);
    }

    /**
     * Get the current center ID from this thread
     * @return Current center ID or null if not set
     */
    public static String getCenterId() {
        return CONTEXT.get();
    }

    /**
     * Clear the center context for this thread
     */
    public static void clear() {
        log.debug("Clearing center context");
        CONTEXT.remove();
    }
}