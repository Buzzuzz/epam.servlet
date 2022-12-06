package dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DataSource class to get connection to database. <br>
 * Config file (default) for database connection located
 * <a href="/src/main/resources/config/db.properties">here.</a> <br>
 * For connection pool used HikariCP.
 */
@Log4j2
public class DataSource {
    private static HikariConfig config;
    private static HikariDataSource ds;

    private static String path = "C:/Users/Admin/IdeaProjects/epam.servlet/src/main/resources/config/db.properties";

    static {
        try {
            config = new HikariConfig(path);
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            log.fatal("Can't config connection pool");
            throw new DAOException("Can't config connection pool", e);
        }
    }

    // Suppress constructor
    private DataSource() {
    }

    /**
     * Getter for connection from connection pool
     *
     * @return {@link Connection} to database defined in config file
     * @throws SQLException In case Hikari couldn't establish connection
     */
    public static Connection getConnection() throws DAOException {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Can't create connection to database");
            throw new DAOException("Can't create connection to database", e);
        }
    }
}
