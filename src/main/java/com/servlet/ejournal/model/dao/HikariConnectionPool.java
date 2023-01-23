package com.servlet.ejournal.model.dao;

import com.servlet.ejournal.exceptions.DAOException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DataSource class to get connection to database. <br>
 * Config file (default) for database connection located
 * <a href="/src/main/resources/config/db.properties">here.</a> <br>
 * For connection pool used HikariCP.
 */
@Log4j2
public class HikariConnectionPool {
    private static final HikariConfig config;
    private static final javax.sql.DataSource ds;
    private static final String PATH = "/db.properties";

    static {
        try {
            log.trace("Connection pool configuration...");
            config = new HikariConfig(PATH);
            ds = new HikariDataSource(config);
            log.trace("Connection pool configured successful!");
        } catch (Exception e) {
            e.printStackTrace();
            log.fatal("Can't configure connection pool!");
            throw new DAOException("Can't config connection pool", e);
        }
    }

    // Suppress constructor
    private HikariConnectionPool() {
    }

    /**
     * Getter for connection from connection pool
     *
     * @return {@link Connection} to database defined in config file
     * @throws DAOException In case Hikari couldn't establish connection
     */
    public static Connection getConnection() throws DAOException {
        try {
            log.trace("Acquiring connection...");
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Can't create connection to database", e);
            throw new DAOException("Can't create connection to database", e);
        }
    }

    public static void close(AutoCloseable closeable) throws DAOException {
        try {
            if (closeable != null) {
                closeable.close();
                log.trace(closeable.getClass().getName() + " closed!");
            }
        } catch (Exception e) {
            log.error("Can't close connection | statement | resultSet", e);
            throw new DAOException("Can't close connection | statement | resultSet", e);
        }
    }

    /**
     * Code snippet to close several {@link AutoCloseable statements} in one line of code
     * (yeah, I definitely love shortening code). Order of arguments in method is important
     * (the first one passed will be closed also at first).
     *
     * @param args Varargs to be closed
     */
    public static void closeAll(AutoCloseable... args) throws DAOException {
        for (AutoCloseable arg : args) {
            close(arg);
        }
    }

    /**
     * Method to try {@link Connection#rollback() rollback} transaction (and shorten boilerplate code)
     *
     * @param con {@link Connection} on which will be tried rollback
     */
    public static void rollback(Connection con) throws DAOException {
        try {
            con.rollback();
            log.trace("Transaction rolled back!");
        } catch (Exception e) {
            log.error("Can't rollback transaction");
            throw new DAOException("Can't rollback transaction", e);
        }
    }

    public static void commit(Connection con) {
        try {
            con.commit();
            log.trace("Transaction committed!");
        } catch (SQLException e) {
            log.error("Cant' commit!", e);
            throw new DAOException("Can't commit!", e);
        }
    }

    public static void setAutoCommit(Connection con, boolean type) {
        try {
            if (con != null) {
                con.setAutoCommit(type);
                log.trace("AutoCommit Type changed!");
            }
        } catch (SQLException e) {
            log.error("Can't set connection commit type to " + type, e);
            throw new DAOException("Can't set connection commit type to " + type, e);
        }
    }
}
