package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.User;
import com.servlet.ejournal.model.entities.UserType;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.utils.SqlUtil;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.model.dao.HikariConnectionPool.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;


/**
 * UserDAO class is realization of DAO layer for user entity.
 * Contains methods from {@link DAO} to retrieve data about users from database
 * and to write (create new) users to database.
 */
@Log4j2
public class UserDAO implements DAO<User> {

    //Suppress constructor
    private UserDAO() {
    }

    /**
     * Method to acquire instance of this class (one and only one, singleton pattern)
     *
     * @return {@link DAO} implementation for specified entity (Currently, {@link UserDAO}).
     */
    public static UserDAO getInstance() {
        return Holder.dao;
    }

    /**
     * Nested Holder class that holds instance of {@link DAO} implementation (Currently, UserDAO).
     * Implementation of Singleton pattern.
     */
    private static class Holder {
        private static final UserDAO dao = new UserDAO();
    }

    public Optional<User> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_USER_BY_ID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createUserObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Can't get user from database, id: " + id, e);
            throw new DAOException("Can't get user from database, id: " + id, e);
        } finally {
            close(resultSet);
        }
    }

    public Optional<User> getByEmail(Connection con, String email) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_USER_BY_EMAIL)) {
            statement.setString(1, email);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createUserObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Can't get user from database, email: " + email, e);
            throw new DAOException("Can't get user from database, email: " + email, e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public Collection<User> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<User> users = new ArrayList<>();
        String query = SqlUtil.getAllEntitiesQuery(USER_TABLE, limit, offset, sorting, filters);

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                users.add(createUserObject(resultSet));
            }
            return users;
        } catch (SQLException e) {
            log.error("Can't get all users from database", e);
            throw new DAOException("Can't get all users from database", e);
        }
    }

    @Override
    public long update(Connection con, User user) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            log.info(user);
            statement.setLong(setUserData(user, statement) + 1, user.getU_id());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update user, id: " + user.getU_id(), e);
            throw new DAOException("Can't update user, id: " + user.getU_id(), e);
        }
    }

    @Override
    public long delete(Connection con, long id) {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.DELETE_USER)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            String message = String.format("Can't delete user, id: %s", id);
            log.error(message, e);
            throw new DAOException(message, e);
        }
    }

    @Override
    public long save(Connection con, User user) {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.CREATE_USER, Statement.RETURN_GENERATED_KEYS)) {
            setUserData(user, statement);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add user to database", e);
            throw new DAOException("Can't add user to database", e);
        } finally {
            close(resultSet);
        }
    }

    private User createUserObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new User(
                resultSet.getLong(++k),
                resultSet.getString(++k),
                resultSet.getString(++k),
                resultSet.getString(++k),
                resultSet.getString(++k),
                resultSet.getString(++k),
                UserType.valueOf(resultSet.getString(++k)),
                resultSet.getBoolean(++k),
                resultSet.getBoolean(++k));
    }

    private int setUserData(User user, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, user.getEmail());
        statement.setString(++k, user.getPassword());
        statement.setString(++k, user.getFirst_name());
        statement.setString(++k, user.getLast_name());
        statement.setString(++k, user.getPhone());
        statement.setString(++k, user.getUser_type().name());
        statement.setBoolean(++k, user.is_blocked());
        statement.setBoolean(++k, user.isSend_notification());
        return k;
    }
}
