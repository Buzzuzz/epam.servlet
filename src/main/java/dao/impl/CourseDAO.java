package dao.impl;

import static dao.ConnectionUtils.*;

import dao.*;
import entities.Course;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * CourseDAO class is implementation of {@link dao.DAO} interface
 * for Course table from DataBase (Course entity specified).
 */
@Log4j2
public class CourseDAO implements DAO<Course> {

    @Override
    public Optional<Course> get(long id) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Course course = null;

        try {
            con = DataSource.getConnection();
            statement = con.prepareStatement(SQLQueries.FIND_COURSE_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                course = new Course(resultSet.getLong("c_id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getTimestamp("start_date"), resultSet.getTimestamp("end_date"));
            }
        } catch (Exception e) {
            log.error("Can't get course from database", e);
            throw new DAOException("Can;t get course from database", e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return Optional.ofNullable(course);
    }

    @Override
    public Collection<Course> getAll() {
        List<Course> courses = new ArrayList<>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            con = DataSource.getConnection();
            statement = con.prepareStatement(SQLQueries.FIND_ALL_COURSES_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(resultSet.getLong("c_id")).ifPresent(courses::add);
            }
        } catch (Exception e) {
            log.error("Can't get all courses", e);
            throw new DAOException("Can't get all courses", e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return courses;
    }

    @Override
    public long update(Course entity) {
        Connection con = null;
        PreparedStatement statement = null;
        long affectedRows;

        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(SQLQueries.UPDATE_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, entity.getName());
            statement.setString(k++, entity.getDescription());
            statement.setTimestamp(k++, entity.getStart_date());
            statement.setTimestamp(k++, entity.getEnd_date());
            statement.setLong(k, entity.getC_id());

            affectedRows = statement.executeUpdate();
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't update course " + entity.getC_id(), e);
            throw new DAOException("Can't update course " + entity.getC_id(), e);
        } finally {
            closeAll(statement, con);
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
            statement = con.prepareStatement(SQLQueries.DELETE_COURSE, Statement.RETURN_GENERATED_KEYS);
            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't delete specified course " + id, e);
            throw new DAOException("Can't delete specified course " + id, e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return affectedRows;
    }

    @Override
    public long save(Course entity) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedID;

        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(SQLQueries.CREATE_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, entity.getName());
            statement.setString(k++, entity.getDescription());
            statement.setTimestamp(k++, entity.getStart_date());
            statement.setTimestamp(k, entity.getEnd_date());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedID = resultSet.getLong(1);
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't create specified course" + entity.getC_id(), e);
            throw new DAOException("Can't create specified course " + entity.getC_id(), e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return generatedID;
    }
}
