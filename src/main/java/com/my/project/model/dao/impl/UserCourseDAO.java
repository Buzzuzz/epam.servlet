package com.my.project.model.dao.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.SQLQueries;
import com.my.project.exceptions.DAOException;
import com.my.project.utils.SqlUtil;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.dao.DAO;
import com.my.project.model.entities.UserCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.my.project.model.dao.DataSource.*;

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
            statement = con.prepareStatement(SQLQueries.FIND_USER_COURSE_BY_C_ID_FINAL_MARK);

            int k = 0;
            statement.setLong(++k, id);
            statement.setLong(++k, -1);
            resultSet = statement.executeQuery();

            k = 0;
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

    public Optional<UserCourse> get(Connection con, long courseId, long userId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        UserCourse userCourse = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_USER_COURSE_BY_C_ID);

            int k = 0;
            statement.setLong(++k, courseId);
            statement.setLong(++k, userId);
            resultSet = statement.executeQuery();

            k = 0;
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
    public Collection<UserCourse> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) {
        List<UserCourse> userCourseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String temp = SqlUtil.getEntityPaginationQuery(AttributeConstants.USER_COURSE_TABLE, filters);
            temp = temp.replaceFirst("\\?", sorting);
            statement = con.prepareStatement(temp);

            int k = 0;
            statement.setInt(++k, limit);
            statement.setInt(++k, offset);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                k = 0;
                userCourseList.add(new UserCourse(
                        resultSet.getLong(++k),
                        resultSet.getLong(++k),
                        resultSet.getLong(++k),
                        resultSet.getTimestamp(++k),
                        resultSet.getDouble(++k)
                ));
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