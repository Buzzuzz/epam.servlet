package dao;

import lombok.extern.log4j.Log4j2;

/**
 * Class to handle closing {@link java.sql.Connection connections},
 * {@link java.sql.Statement statements} and {@link java.sql.ResultSet result sets}
 * (shortens writing of boilerplate code).
 */
@Log4j2
public class ConnectionUtils {
    // Suppress constructor
    private ConnectionUtils() {}

    /**
     * Method that replaces try/catch boilerplate code when closing connection, etc.
     * @param closeable {@link AutoCloseable} object that will be tried to close
     */
    public static void close(AutoCloseable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            log.error("Can't close connection | statement | resultSet");
        }
    }
}
