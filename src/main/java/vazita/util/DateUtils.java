package vazita.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utility class for date and time operations
 */
public class DateUtils {
    
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    /**
     * Convert string to date using default format
     * @param dateStr Date string in yyyy-MM-dd format
     * @return Date object
     * @throws ParseException if date cannot be parsed
     */
    public static Date stringToDate(String dateStr) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.parse(dateStr);
    }
    
    /**
     * Convert date to string using default format
     * @param date Date to convert
     * @return Formatted date string
     */
    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }
    
    /**
     * Convert string to LocalDate
     * @param dateStr Date string in yyyy-MM-dd format
     * @return LocalDate object
     */
    public static LocalDate stringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(dateStr, formatter);
    }
    
    /**
     * Convert LocalDate to string
     * @param date LocalDate to convert
     * @return Formatted date string
     */
    public static String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return date.format(formatter);
    }
    
    /**
     * Convert string to LocalDateTime
     * @param dateTimeStr DateTime string in yyyy-MM-dd HH:mm:ss format
     * @return LocalDateTime object
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
    
    /**
     * Convert LocalDateTime to string
     * @param dateTime LocalDateTime to convert
     * @return Formatted datetime string
     */
    public static String localDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }
    
    /**
     * Get current date as string
     * @return Current date as string
     */
    public static String getCurrentDateAsString() {
        return localDateToString(LocalDate.now());
    }
    
    /**
     * Get current datetime as string
     * @return Current datetime as string
     */
    public static String getCurrentDateTimeAsString() {
        return localDateTimeToString(LocalDateTime.now());
    }
}