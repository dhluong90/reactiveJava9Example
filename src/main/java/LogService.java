import static java.lang.Thread.currentThread;

public class LogService {
    private static final String LOG_MESSAGE_FORMAT = "Publisher >> [%s] %s%n";

    public static void log(String message, Object... args) {
        String fullMessage = String.format(LOG_MESSAGE_FORMAT, currentThread().getName(), message);
        System.out.printf(fullMessage, args);
    }
}
