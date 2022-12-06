package dao;

import entities.User;
import entities.UserType;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * UserDAO class is realization of DAO layer for user entity.
 * Contains methods to retrieve data about users from database
 * and to write (create new) users to database.
 */
@Log4j2
@Slf4j
public class UserDAO {
    // Suppress constructor
    private UserDAO() {
    }

    public static User findUserByID(long ID) {
        User user = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try (Connection con = DataSource.getConnection()) {
            statement = con.prepareStatement(SQLQueries.FIND_USER_BY_ID);
            statement.setLong(1, ID);
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
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                log.error("Can't close one of statements (resultSet, connection etc.)");
            }

        }

        return user;
    }

    public static long createUser(User user) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedID;
        log.info("started user creating");
        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
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
            con.commit();
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ignored) {}
            log.error("Can't add user to database");
            throw new DAOException("Can't add user to database", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                log.error("Can't close one of statements (connection, resultSet etc.)");
            }
        }

        return generatedID;
    }
}
