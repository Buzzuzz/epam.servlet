package model.dao.impl;

import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import model.dao.DAOException;
import model.dao.SQLQueries;
import model.entities.User;
import model.entities.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static model.dao.DataSource.*;

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

    public Optional<User> get(Connection con, long id) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            statement = con.prepareStatement(SQLQueries.FIND_USER_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = new User(resultSet.getLong("u_id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setPhone(resultSet.getString("phone"));

                String userType = resultSet.getString("user_type");
                if (userType.equals(UserType.ADMINISTRATOR.name())) user.setUser_type(UserType.ADMINISTRATOR);
                if (userType.equals(UserType.TEACHER.name())) user.setUser_type(UserType.TEACHER);
                if (userType.equals(UserType.STUDENT.name())) user.setUser_type(UserType.STUDENT);

                user.set_blocked(resultSet.getBoolean("is_blocked"));
                user.setSend_notification(resultSet.getBoolean("send_notification"));
            }

        } catch (Exception e) {
            log.error("Can't get user from database");
            throw new DAOException("Can't get user from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(user);
    }

    public Optional<User> getByEmail(Connection con, String email) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        User user = null;
        try {
            statement = con.prepareStatement(SQLQueries.FIND_USER_BY_EMAIL);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                user = new User(resultSet.getLong("u_id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setFirst_name(resultSet.getString("first_name"));
                user.setLast_name(resultSet.getString("last_name"));
                user.setPhone(resultSet.getString("phone"));

                String userType = resultSet.getString("user_type");
                if (userType.equals(UserType.ADMINISTRATOR.name())) user.setUser_type(UserType.ADMINISTRATOR);
                if (userType.equals(UserType.TEACHER.name())) user.setUser_type(UserType.TEACHER);
                if (userType.equals(UserType.STUDENT.name())) user.setUser_type(UserType.STUDENT);

                user.set_blocked(resultSet.getBoolean("is_blocked"));
                user.setSend_notification(resultSet.getBoolean("send_notification"));
            }

        } catch (Exception e) {
            log.error("Can't get user from database", e);
            throw new DAOException("Can't get user from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> getAll(Connection con) {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            statement = con.prepareStatement(SQLQueries.FIND_ALL_USERS_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(con, resultSet.getLong("u_id")).ifPresent(users::add);
            }

        } catch (Exception e) {
            log.error("Can't get all users from database");
            throw new DAOException("Can't get all users from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return users;
    }

    @Override
    public long update(Connection con, User user) {
        PreparedStatement statement = null;
        long affectedRows;
        try {
            statement = con.prepareStatement(SQLQueries.UPDATE_USER, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setString(++k, user.getEmail());
            statement.setString(++k, user.getPassword());
            statement.setString(++k, user.getFirst_name());
            statement.setString(++k, user.getLast_name());
            statement.setString(++k, user.getPhone());
            statement.setString(++k, user.getUser_type().name());
            statement.setBoolean(++k, user.is_blocked());
            statement.setBoolean(++k, user.isSend_notification());
            statement.setLong(++k, user.getU_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update user");
            throw new DAOException("Can't update user", e);
        } finally {
            close(statement);
        }

        return affectedRows;
    }

    @Override
    public long delete(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long affectedRows;
        try {
            statement = con.prepareStatement(SQLQueries.DELETE_USER, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete user");
            throw new DAOException("Can't delete user", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, User user) {

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedID;
        try {
            statement = con.prepareStatement(SQLQueries.CREATE_USER, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setString(++k, user.getEmail());
            statement.setString(++k, user.getPassword());
            statement.setString(++k, user.getFirst_name());
            statement.setString(++k, user.getLast_name());
            statement.setString(++k, user.getPhone());
            statement.setString(++k, user.getUser_type().name());
            statement.setBoolean(++k, user.is_blocked());
            statement.setBoolean(++k, user.isSend_notification());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedID = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add user to database");
            throw new DAOException("Can't add user to database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return generatedID;
    }
}
