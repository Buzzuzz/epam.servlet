package model.dao.impl;

import constants.SQLQueries;
import exceptions.DAOException;
import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import model.entities.UserCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static model.dao.DataSource.*;

@Log4j2
public class UserCourseDAO implements DAO<UserCourse> {
    public static UserCourseDAO getInstance() {
        return Holder.dao;
    }

    private static class Holder {
        private static final UserCourseDAO dao = new UserCourseDAO();
    }
    @Override
    public Optional<UserCourse> get(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        UserCourse userCourse = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_USER_COURSE_BY_C_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            int k = 0;
            while (resultSet.next()) {
                userCourse = new UserCourse(
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
        } catch (Exception e) {
            log.error("Can't get join table user-course", e);
            throw new DAOException("Can't get join table user-course", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(userCourse);
    }

    @Override
    public Collection<UserCourse> getAll(Connection con) {
        List<UserCourse> userCourseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_ALL_COURSES_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(con, resultSet.getLong(1)).ifPresent(userCourseList::add);
            }
        } catch (Exception e) {
            log.error("Can't get all user_courses from database", e);
            throw new DAOException("Can't get all user_courses from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return userCourseList;
    }

    @Override
    public long update(Connection con, UserCourse userCourse) {
        PreparedStatement statement = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(SQLQueries.UPDATE_USER_COURSE);

            int k = 0;
            statement.setLong(++k, userCourse.getU_id());
            statement.setLong(++k, userCourse.getC_id());
            statement.setTimestamp(++k, userCourse.getRegistration_date());
            statement.setDouble(++k, userCourse.getFinal_mark());
            statement.setLong(++k, userCourse.getU_c_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update user_course: " + userCourse.getU_c_id(), e);
            throw new DAOException("Can't update user_course: " + userCourse.getU_c_id());
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
            statement = con.prepareStatement(SQLQueries.DELETE_USER_COURSE, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete user_course: " + id, e);
            throw new DAOException("Can't delete user_course: " + id);
        } finally {
            closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, UserCourse userCourse) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedId;

        try {
            statement = con.prepareStatement(SQLQueries.CREATE_USER_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setLong(++k, userCourse.getU_id());
            statement.setLong(++k, userCourse.getC_id());
            statement.setTimestamp(++k, userCourse.getRegistration_date());
            statement.setDouble(++k, userCourse.getFinal_mark());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedId = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't insert user_course", e);
            throw new DAOException("Can't insert user_course", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return generatedId;
    }
}
