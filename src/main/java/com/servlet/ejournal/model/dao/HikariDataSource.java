package com.servlet.ejournal.model.dao;

import com.servlet.ejournal.annotations.Transaction;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.exceptions.TransactionException;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    /**
     * Method to close AutoCloseable interface implementation (without catching exception)
     *
     * @param closeable Object to close
     * @throws DAOException In case of unsuccessful close operation (already closed, etc.)
     */
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

    /**
     * Method to run block of code in transaction
     * @param classObject Class instance, where transaction block is located
     * @param methodName Name of method to run as transaction
     * @param args Arguments, needed for method
     * @return Return value of that block of code
     * @param <T> Return type of return value
     * @throws TransactionException In case transaction cannot be done successful
     */
    public <T> T runTransaction(Object classObject, String methodName, Object... args) throws TransactionException {
        Connection con = null;
        T returnValue;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            List<Object> argsList = new ArrayList<>(Arrays.asList(args));
            argsList.add(0, con);

            Class<?> clazz = classObject.getClass();
            for (Method classMethod : clazz.getDeclaredMethods()) {
                if (classMethod.isAnnotationPresent(Transaction.class) && classMethod.getName().equals(methodName)) {
                    classMethod.setAccessible(true);
                    returnValue = (T) classMethod.invoke(classObject, argsList.toArray());
                    con.commit();
                    con.setAutoCommit(true);
                    return returnValue;
                }
            }
            throw new TransactionException("No methods that can satisfy current conditions (@Transaction annotation, provided method name)");
        } catch (Exception e) {
            rollback(con);
            log.error(e.getMessage(), e);
            throw new TransactionException("Can't run transaction properly, rollback...", e);
        }
    }

    /**
     * Method to rollback changes made on specified connection
     * @param con {@link Connection, where changes were made}
     * @throws DAOException In case rollback cannot be done successful
     */
    private void rollback(Connection con) throws DAOException {
        try {
            if (con != null) {
                con.rollback();
            }
            log.trace("Transaction rolled back!");
        } catch (Exception e) {
            log.error("Can't rollback transaction");
            throw new DAOException("Can't rollback transaction", e);
        }
    }
}
