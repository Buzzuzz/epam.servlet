package dao.impl;

import dao.DAO;
import dao.DAOException;
import dao.DataSource;
import dao.SQLQueries;
import entities.User;
import entities.UserType;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * UserDAO class is realization of DAO layer for user entity.
 * Contains methods from {@link DAO} to retrieve data about users from database
 * and to write (create new) users to database.
 */
@Log4j2
@NoArgsConstructor
public class UserDAO implements DAO<User> {
    public Optional<User> get(long id) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        User user = null;
        try (Connection con = DataSource.getConnection()) {
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

        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> getAll() {
        List<User> users = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try (Connection con = DataSource.getConnection()) {
            statement = con.prepareStatement(SQLQueries.FIND_ALL_USERS_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(resultSet.getLong("u_id")).ifPresent(users::add);
            }

        } catch (Exception e) {
            log.error("Can't get all users from database");
            throw new DAOException("Can't get all users from database", e);
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

        return users;
    }

    @Override
    public long update(User user) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long affectedRows;
        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
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
            con.commit();
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ignored) {
            }
            log.error("Can't update user");
            throw new DAOException("Can't update user", e);
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

        return affectedRows;
    }

    @Override
    public long delete(long id) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long affectedRows;
        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(SQLQueries.DELETE_USER, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);

            affectedRows = statement.executeUpdate();
//            resultSet = statement.getGeneratedKeys();
//            resultSet.next();
//            affectedRows = resultSet.getLong(1);
            con.commit();
        } catch (Exception e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ignored) {
            }
            log.error("Can't delete user");
            throw new DAOException("Can't delete user", e);
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

        return affectedRows;
    }

    @Override
    public long save(User user) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedID;
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
            } catch (SQLException ignored) {
            }
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
