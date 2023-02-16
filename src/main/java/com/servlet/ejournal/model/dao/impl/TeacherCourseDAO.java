package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.interfaces.IntermediateTable;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.TeacherCourse;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.model.dao.HikariDataSource.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.SQLQueries.*;

@Log4j2
public class TeacherCourseDAO implements DAO<TeacherCourse>, IntermediateTable<TeacherCourse> {
    // Suppress constructor
    private TeacherCourseDAO() {
    }

    private static class Holder {
        private static final TeacherCourseDAO dao = new TeacherCourseDAO();
    }

    public static TeacherCourseDAO getInstance() {
        return Holder.dao;
    }

    @Override
    public Optional<TeacherCourse> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.FIND_TEACHER_COURSE_BY_ID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createTeacherCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Can't get teacher_course from database, course id: " + id, e);
            throw new DAOException("Can't get teacher_course from database, course id: " + id, e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public Optional<TeacherCourse> get(Connection con, long teacherId, long courseId) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(FIND_TEACHER_COURSE_BY_TCH_ID_C_ID)) {
            statement.setLong(1, teacherId);
            statement.setLong(2, courseId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createTeacherCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String msg = String.format("Can't get teacher_course from database | course id: %s, teacher id: %s", courseId, teacherId);
            log.error(msg, e);
            throw new DAOException(msg, e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public Collection<TeacherCourse> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<TeacherCourse> teacherCourses = new ArrayList<>();
        String query = getAllEntitiesQuery(TEACHER_COURSE_TABLE, limit, offset, sorting, filters);

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                teacherCourses.add(createTeacherCourseObject(resultSet));
            }
            return teacherCourses;
        } catch (SQLException e) {
            log.error("Can't get all teacher-courses from database", e);
            throw new DAOException("Can't get all teacher-courses from database", e);
        }
    }

    @Override
    public long update(Connection con, TeacherCourse teacherCourse) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.UPDATE_TEACHER_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, teacherCourse.getTch_id());
            statement.setLong(2, teacherCourse.getC_id());
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't update teacher_course(tch_id): " + teacherCourse.getTch_id());
            throw new DAOException("Can't update teacher_course(tch_id): " + teacherCourse.getTch_id(), e);
        }
    }

    @Override
    public long delete(Connection con, long id) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(SQLQueries.DELETE_TEACHER_COURSE)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            log.error("Can't delete specified teacher_course" + id, e);
            throw new DAOException("Can't delete specified teacher_course", e);
        }
    }

    @Override
    public long save(Connection con, TeacherCourse teacherCourse) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.CREATE_TEACHER_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, teacherCourse.getTch_id());
            statement.setLong(2, teacherCourse.getC_id());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add teacher_course to database", e);
            throw new DAOException("Can't add teacher_course to database", e);
        } finally {
            close(resultSet);
        }
    }

    private TeacherCourse createTeacherCourseObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new TeacherCourse(
                resultSet.getLong(++k),
                resultSet.getLong(++k),
                resultSet.getLong(++k));
    }
}
