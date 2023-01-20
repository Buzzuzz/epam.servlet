package com.my.project.model.dao.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.SQLQueries;
import com.my.project.exceptions.DAOException;
import com.my.project.model.dao.DataSource;
import com.my.project.model.entities.Course;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.dao.DAO;
import com.my.project.utils.PaginationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


/**
 * CourseDAO class is implementation of {@link DAO} interface
 * for Course table from DataBase (Course entity specified).
 */
@Log4j2
public class CourseDAO implements DAO<Course> {
    // Suppress constructor
    private CourseDAO() {
    }

    /**
     * Method to acquire specified {@link DAO} implementation ({@link Course} in this case).
     * Singleton pattern
     *
     * @return {@link DAO} implementation for {@link Course} entity.
     */
    public static CourseDAO getInstance() {
        return Holder.dao;
    }

    private static class Holder {
        private static final CourseDAO dao = new CourseDAO();
    }

    @Override
    public Optional<Course> get(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Course course = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_COURSE_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                course = new Course(
                        resultSet.getLong("c_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getTimestamp("start_date"),
                        resultSet.getTimestamp("end_date"),
                        resultSet.getLong("duration"));
            }
        } catch (Exception e) {
            log.error("Can't get course from database", e);
            throw new DAOException("Can;t get course from database", e);
        } finally {
            DataSource.closeAll(resultSet, statement);
        }

        return Optional.ofNullable(course);
    }

    @Override
    public Collection<Course> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) {
        List<Course> courses = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String temp = PaginationUtil.getEntityPaginationQuery(SQLQueries.JOIN_COURSE_TOPIC_USER_TEACHER_TABLE, filters);
            temp = temp.replaceFirst("\\?", sorting);

            statement = con.prepareStatement(temp);

            int k = 0;
            statement.setInt(++k, limit);
            statement.setInt(++k, offset);
            log.info("course dao query " + statement);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(con, resultSet.getLong(AttributeConstants.COURSE_ID)).ifPresent(courses::add);
            }
        } catch (Exception e) {
            log.error("Can't get all courses", e);
            throw new DAOException("Can't get all courses", e);
        } finally {
            DataSource.closeAll(resultSet, statement);
        }

        return courses;
    }

    @Override
    public long update(Connection con, Course entity) {
        PreparedStatement statement = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(SQLQueries.UPDATE_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setString(++k, entity.getName());
            statement.setString(++k, entity.getDescription());
            statement.setTimestamp(++k, entity.getStart_date());
            statement.setTimestamp(++k, entity.getEnd_date());
            statement.setLong(++k, entity.getDuration());
            statement.setLong(++k, entity.getC_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update course " + entity.getC_id(), e);
            throw new DAOException("Can't update course " + entity.getC_id(), e);
        } finally {
            DataSource.close(statement);
        }

        return affectedRows;
    }

    @Override
    public long delete(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(SQLQueries.DELETE_COURSE, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete specified course " + id, e);
            throw new DAOException("Can't delete specified course " + id, e);
        } finally {
            DataSource.closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, Course entity) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedID;

        try {
            statement = con.prepareStatement(SQLQueries.CREATE_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setString(++k, entity.getName());
            statement.setString(++k, entity.getDescription());
            statement.setTimestamp(++k, entity.getStart_date());
            statement.setTimestamp(++k, entity.getEnd_date());
            statement.setLong(++k, entity.getDuration());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedID = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't create specified course" + entity.getC_id(), e);
            throw new DAOException("Can't create specified course " + entity.getC_id(), e);
        } finally {
            DataSource.closeAll(resultSet, statement);
        }

        return generatedID;
    }
}
