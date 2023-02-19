package com.servlet.ejournal.annotations.handlers;

import com.servlet.ejournal.annotations.Transaction;
import com.servlet.ejournal.exceptions.TransactionException;
import com.servlet.ejournal.model.dao.HikariDataSource;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class TransactionHandler {
    private static TransactionHandler instance;
    private final HikariDataSource source;

    // Suppress constructor
    private TransactionHandler(HikariDataSource source) {
        this.source = source;
    }

    public static synchronized TransactionHandler getInstance(HikariDataSource source) {
        if (instance == null) {
            instance = new TransactionHandler(source);
        }
        return instance;
    }

    public <T> T runTransaction(Object classObject, String methodName, Object... args) throws TransactionException {
        Connection con = null;
        T returnValue;
        try {
            con = source.getConnection();
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
            source.rollback(con);
            log.error(e.getMessage(), e);
            throw new TransactionException("Can't run transaction properly, rollback...", e);
        }
    }
}
