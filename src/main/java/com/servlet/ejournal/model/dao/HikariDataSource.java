package com.servlet.ejournal.model.dao;

import com.servlet.ejournal.exceptions.DAOException;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * DataSource class to get connection to database. <br>
 * Config file (default) for database connection located
 * <a href="src/main/resources/db/db.properties">here.</a> <br>
 * For connection pool used HikariCP.
 */
@Log4j2
public class HikariDataSource {
    private static HikariDataSource instance;
    private final javax.sql.DataSource ds;

    // Suppress constructor
    private HikariDataSource(String path) {
        try {
            log.trace("Connection pool configuration...");
            HikariConfig config = new HikariConfig(path);
            ds = new com.zaxxer.hikari.HikariDataSource(config);
            log.trace("Connection pool configured successful!");
        } catch (Exception e) {
            log.fatal("Can't configure connection pool!", e);
            throw new DAOException("Can't config connection pool", e);
        }
    }

    public static synchronized HikariDataSource getInstance(String path) {
        if (instance == null) {
            instance = new HikariDataSource(path);
        }
        return instance;
    }

    /**
     * Getter for connection from connection pool
     *
     * @return {@link Connection} to database defined in config file
     * @throws DAOException In case Hikari couldn't establish connection
     */
    public Connection getConnection() throws DAOException {
        try {
            log.trace("Acquiring connection...");
            return ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("Can't create connection to database", e);
            throw new DAOException("Can't create connection to database", e);
        }
    }

    public void close(AutoCloseable closeable) throws DAOException {
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

    public void rollback(Connection con) throws DAOException {
        try {
            con.rollback();
            log.trace("Transaction rolled back!");
        } catch (Exception e) {
            log.error("Can't rollback transaction");
            throw new DAOException("Can't rollback transaction", e);
        }
    }

    public void commit(Connection con) {
        try {
            con.commit();
            log.trace("Transaction committed!");
        } catch (SQLException e) {
            log.error("Cant' commit!", e);
            throw new DAOException("Can't commit!", e);
        }
    }

    public void setAutoCommit(Connection con, boolean type) {
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
