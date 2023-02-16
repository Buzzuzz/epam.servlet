package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.interfaces.IntermediateTable;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.UserCourse;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.model.dao.HikariDataSource.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class UserCourseDAO implements DAO<UserCourse>, IntermediateTable<UserCourse> {
    private static UserCourseDAO instance;
    private final HikariDataSource source;

    private UserCourseDAO(HikariDataSource source) {
        this.source = source;
    }

    /**
     * Method to acquire instance of current class
     *
     * @param source - {@link HikariDataSource DataSource} to get connection to database
     * @return instance of {@link DAO dao} implementation for specified entity {@link UserCourse}
     */

    public static synchronized UserCourseDAO getInstance(HikariDataSource source) {
        if (instance == null) {
            instance = new UserCourseDAO(source);
        }
        return instance;
    }


    @Override
    public Optional<UserCourse> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_USER_COURSE_BY_C_ID_FINAL_MARK)) {
            statement.setLong(1, id);
            statement.setLong(2, -1);
            resultSet = statement.executeQuery();
            return resultSet.next() ? Optional.of(createUserCourseObject(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            log.error("Can't get join table user-course, id: " + id, e);
            throw new DAOException("Can't get join table user-course, id: " + id, e);
        } finally {
            source.close(resultSet);
        }
    }

    @Override
    public Optional<UserCourse> get(Connection con, long courseId, long userId) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_USER_COURSE_BY_C_ID)) {
            statement.setLong(1, courseId);
            statement.setLong(2, userId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createUserCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String msg = String.format("Can't get join table user-course, user id: %s, course id: %s", userId, courseId);
            log.error(msg, e);
            throw new DAOException(msg, e);
        } finally {
            source.close(resultSet);
        }
    }

    @Override
    public Collection<UserCourse> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<UserCourse> userCourseList = new ArrayList<>();
        String query = getAllEntitiesQuery(USER_COURSE_TABLE, limit, offset, sorting, filters);

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                userCourseList.add(createUserCourseObject(resultSet));
            }
            return userCourseList;
        } catch (SQLException e) {
            log.error("Can't get all user_courses from database", e);
            throw new DAOException("Can't get all user_courses from database", e);
        }
    }

    @Override
    public long update(Connection con, UserCourse userCourse) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_USER_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(setUserCourseData(userCourse, statement) + 1, userCourse.getU_c_id());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update user_course, id: " + userCourse.getU_c_id(), e);
            throw new DAOException("Can't update user_course, id: " + userCourse.getU_c_id());
        }
    }

    @Override
    public long delete(Connection con, long id) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.DELETE_USER_COURSE)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete user_course, id: " + id, e);
            throw new DAOException("Can't delete user_course, id: " + id);
        }
    }

    @Override
    public long save(Connection con, UserCourse userCourse) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.CREATE_USER_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            setUserCourseData(userCourse, statement);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (SQLException e) {
            log.error("Can't insert user_course", e);
            throw new DAOException("Can't insert user_course", e);
        } finally {
            source.close(resultSet);
        }
    }

    private UserCourse createUserCourseObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new UserCourse(
                // u_c_id
                resultSet.getLong(++k),
                // u_id
                resultSet.getLong(++k),
                // c_id
                resultSet.getLong(++k),
                // registration_date
                resultSet.getTimestamp(++k),
                // final_mark
                resultSet.getDouble(++k)
        );
    }

    private int setUserCourseData(UserCourse userCourse, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setLong(++k, userCourse.getU_id());
        statement.setLong(++k, userCourse.getC_id());
        statement.setTimestamp(++k, userCourse.getRegistration_date());
        statement.setDouble(++k, userCourse.getFinal_mark());
        return k;
    }
}
