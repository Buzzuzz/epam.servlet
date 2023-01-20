package com.my.project.model.dao.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.SQLQueries;
import com.my.project.exceptions.DAOException;
import com.my.project.utils.SqlUtil;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.dao.DAO;
import com.my.project.model.entities.TeacherCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static com.my.project.model.dao.DataSource.close;
import static com.my.project.model.dao.DataSource.closeAll;

@Log4j2
public class TeacherCourseDAO implements DAO<TeacherCourse> {
    // Suppress constructor
    private TeacherCourseDAO() {}

    private static class Holder {
        private static TeacherCourseDAO dao = new TeacherCourseDAO();
    }

    public static TeacherCourseDAO getInstance() {
        return Holder.dao;
    }
    @Override
    public Optional<TeacherCourse> get(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        TeacherCourse tc = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_TEACHER_COURSE_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Skip primary key
                int k = 1;
                tc = new TeacherCourse(
                        resultSet.getLong(++k),
                        resultSet.getLong(++k));
            }
        } catch (Exception e) {
            log.error("Can't get teacher_course from database");
            throw new DAOException("Can't get teacher_course from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(tc);
    }

    @Override
    public Collection<TeacherCourse> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) {
        List<TeacherCourse> teacherCourses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String temp = SqlUtil.getEntityPaginationQuery(AttributeConstants.TEACHER_COURSE_TABLE, filters);
            temp = temp.replaceFirst("\\?", sorting);
            statement = con.prepareStatement(temp);

            int k = 0;
            statement.setInt(++k, limit);
            statement.setInt(++k, offset);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                k = 0;
                teacherCourses.add(new TeacherCourse(
                        resultSet.getLong(++k),
                        resultSet.getLong(++k)
                ));
            }
            return teacherCourses;
        } catch (Exception e) {
            log.error("Can't get all teacher_courses from database", e);
            throw new DAOException("Can't get all teacher_courses from database", e);
        } finally {
            closeAll(resultSet, statement);
        }
    }

    @Override
    public long update(Connection con, TeacherCourse teacherCourse) {
        PreparedStatement statement = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(SQLQueries.UPDATE_TEACHER_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setLong(++k, teacherCourse.getTch_id());
            statement.setLong(++k, teacherCourse.getC_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update teacher_course(tch_id): " + teacherCourse.getTch_id());
            throw new DAOException("Can't update teacher_course(tch_id): " + teacherCourse.getTch_id(), e);
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
            statement = con.prepareStatement(SQLQueries.DELETE_TEACHER_COURSE, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete specified teacher_course" + id, e);
            throw new DAOException("Can't delete specified teacher_course", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, TeacherCourse teacherCourse) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedId;

        try {
            statement = con.prepareStatement(SQLQueries.CREATE_TEACHER_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setLong(++k, teacherCourse.getTch_id());
            statement.setLong(++k, teacherCourse.getC_id());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedId = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add teacher_course to database", e);
            throw new DAOException("Can't add teacher_course to database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return generatedId;
    }
}
