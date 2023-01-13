package model.dao.impl;

import exceptions.DAOException;
import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import model.entities.TopicCourse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static model.dao.DataSource.*;
import static constants.SQLQueries.*;

@Log4j2
public class TopicCourseDAO implements DAO<TopicCourse> {

    public static TopicCourseDAO getInstance() {
        return Holder.dao;
    }

    private static class Holder {
        private static final TopicCourseDAO dao = new TopicCourseDAO();
    }
    @Override
    public Optional<TopicCourse> get(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        TopicCourse topicCourse = null;

        try {
            statement = con.prepareStatement(FIND_TOPIC_COURSE_BY_C_ID);

            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            int k = 0;
            while (resultSet.next()) {
                topicCourse = new TopicCourse(
                        resultSet.getLong(++k),
                        resultSet.getLong(++k)
                );
            }
        } catch (Exception e) {
            log.error("Can't get topic_course: " + id, e);
            throw new DAOException("Can't get topic_course: " + id, e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(topicCourse);
    }

    @Override
    public Collection<TopicCourse> getAll(Connection con, int limit, int offset, String sorting) {
        List<TopicCourse> topicCourseList = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = con.prepareStatement(FIND_ALL_COURSES_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(con, resultSet.getLong(1)).ifPresent(topicCourseList::add);
            }
        } catch (Exception e) {
            log.error("Can't get all topic_course from database", e);
            throw new DAOException("Can't get all topic_course from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return topicCourseList;
    }

    @Override
    public long update(Connection con, TopicCourse topicCourse) {
        PreparedStatement statement = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(UPDATE_TOPIC_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setLong(++k, topicCourse.getT_id());
            statement.setLong(++k, topicCourse.getC_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update topic_course (c_id): " + topicCourse.getC_id(), e);
            throw new DAOException("Can't update topic_course (c_id): " + topicCourse.getC_id(), e);
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
            statement = con.prepareStatement(DELETE_TOPIC_COURSE, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete topic_course (c_id): " + id, e);
            throw new DAOException("Can't delete topic_course (c_id): " + id, e);
        } finally {
            closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, TopicCourse topicCourse) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedId;

        try {
            statement = con.prepareStatement(CREATE_TOPIC_COURSE, Statement.RETURN_GENERATED_KEYS);

            int k = 0;
            statement.setLong(++k, topicCourse.getT_id());
            statement.setLong(++k, topicCourse.getC_id());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedId = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't insert topic_course", e);
            throw new DAOException("Can't insert topic_course", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return generatedId;
    }
}
