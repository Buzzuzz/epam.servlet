package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.entities.Course;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.interfaces.DAO;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.model.dao.HikariConnectionPool.*;
import static com.servlet.ejournal.constants.SQLQueries.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;


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
    public Optional<Course> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_COURSE_BY_ID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String msg = String.format("Can't get course from database, id: %s", id);
            log.error(msg, e);
            throw new DAOException(msg, e);
        } finally {
            close(resultSet);
        }

    }

    @Override
    public Collection<Course> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<Course> courses = new ArrayList<>();
        String query = getAllEntitiesQuery(JOIN_COURSE_TOPIC_USER_TEACHER_TABLE, limit, offset, sorting, filters);

        log.fatal(query);
        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                get(con, resultSet.getLong(COURSE_ID)).ifPresent(courses::add);
            }
            return courses;
        } catch (SQLException e) {
            log.error("Can't get all courses", e);
            throw new DAOException("Can't get all courses", e);
        }
    }

    @Override
    public long update(Connection con, Course course) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(setCourseData(course, statement) + 1, course.getC_id());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update course " + course.getC_id(), e);
            throw new DAOException("Can't update course " + course.getC_id(), e);
        }
    }

    @Override
    public long delete(Connection con, long id) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.DELETE_COURSE)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't delete specified course, id: " + id, e);
            throw new DAOException("Can't delete specified course, id: " + id, e);
        }
    }

    @Override
    public long save(Connection con, Course course) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.CREATE_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            setCourseData(course, statement);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't create specified course" + course.getC_id(), e);
            throw new DAOException("Can't create specified course " + course.getC_id(), e);
        } finally {
            close(resultSet);
        }
    }

    private Course createCourseObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new Course(
                resultSet.getLong(++k),
                resultSet.getString(++k),
                resultSet.getString(++k),
                resultSet.getTimestamp(++k),
                resultSet.getTimestamp(++k),
                resultSet.getLong(++k));
    }

    private int setCourseData(Course course, PreparedStatement statement) throws SQLException {
        int k = 0;
        statement.setString(++k, course.getName());
        statement.setString(++k, course.getDescription());
        statement.setTimestamp(++k, course.getStart_date());
        statement.setTimestamp(++k, course.getEnd_date());
        statement.setLong(++k, course.getDuration());
        return k;
    }
}
