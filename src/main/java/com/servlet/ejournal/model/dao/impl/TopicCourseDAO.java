package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.interfaces.IntermediateTable;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.TopicCourse;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.constants.SQLQueries.*;
import static com.servlet.ejournal.model.dao.HikariConnectionPool.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class TopicCourseDAO implements DAO<TopicCourse>, IntermediateTable<TopicCourse> {

    public static TopicCourseDAO getInstance() {
        return Holder.dao;
    }

    private static class Holder {
        private static final TopicCourseDAO dao = new TopicCourseDAO();
    }

    @Override
    public Optional<TopicCourse> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(FIND_TOPIC_COURSE_BY_C_ID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createTopicCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            log.error("Can't get topic_course, id: " + id, e);
            throw new DAOException("Can't get topic_course, id: " + id, e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public Optional<TopicCourse> get(Connection con, long topicId, long courseId) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(FIND_TOPIC_COURSE_BY_T_ID_C_ID)) {
            statement.setLong(1, topicId);
            statement.setLong(2, courseId);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(createTopicCourseObject(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            String msg = String.format("Can't get topic-course | topic id: %s, course id: %s", topicId, courseId);
            log.error(msg, e);
            throw new DAOException(msg, e);
        } finally {
            close(resultSet);
        }
    }

    @Override
    public Collection<TopicCourse> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<TopicCourse> topicCourseList = new ArrayList<>();
        String query = getAllEntitiesQuery(TOPIC_COURSE_TABLE, limit, offset, sorting, filters);

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                topicCourseList.add(createTopicCourseObject(resultSet));
            }

            return topicCourseList;
        } catch (SQLException e) {
            log.error("Can't get all topic_course from database", e);
            throw new DAOException("Can't get all topic_course from database", e);
        }
    }

    @Override
    public long update(Connection con, TopicCourse topicCourse) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(UPDATE_TOPIC_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, topicCourse.getT_id());
            statement.setLong(2, topicCourse.getC_id());
            return statement.executeUpdate();
        } catch (SQLException e) {
            String msg = String.format("Can't update topic_course | topic id: %s, course id: %s", topicCourse.getT_id(), topicCourse.getC_id());
            log.error(msg, e);
            throw new DAOException(msg, e);
        }
    }

    @Override
    public long delete(Connection con, long id) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(DELETE_TOPIC_COURSE)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            String msg = String.format("Can't delete topic_course | course id: %s", id);
            log.error(msg, e);
            throw new DAOException(msg, e);
        }
    }

    @Override
    public long save(Connection con, TopicCourse topicCourse) {
        ResultSet resultSet;

        try (PreparedStatement statement = con.prepareStatement(CREATE_TOPIC_COURSE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, topicCourse.getT_id());
            statement.setLong(2, topicCourse.getC_id());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't insert topic_course", e);
            throw new DAOException("Can't insert topic_course", e);
        }
    }

    private TopicCourse createTopicCourseObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new TopicCourse(resultSet.getLong(++k), resultSet.getLong(++k));
    }
}
