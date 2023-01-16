package model.dao.impl;

import constants.AttributeConstants;
import lombok.extern.log4j.Log4j2;
import model.dao.DAO;
import exceptions.DAOException;
import constants.SQLQueries;
import model.entities.Topic;
import utils.PaginationUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

import static model.dao.DataSource.*;


/**
 * TopicDAO class is implementation of {@link DAO} interface for Topic table.
 * (Topic entity specified).
 */
@Log4j2
public class TopicDAO implements DAO<Topic> {
    //Suppress constructor
    private TopicDAO() {
    }

    /**
     * Method to acquire TopicDAO implementation of {@link DAO} interface (singleton pattern).
     *
     * @return {@link DAO} implementation for specified entity ({@link Topic} in this case)
     */
    public static TopicDAO getInstance() {
        return Holder.dao;
    }

    private static class Holder {
        private static final TopicDAO dao = new TopicDAO();
    }

    @Override
    public Optional<Topic> get(Connection con, long id) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Topic topic = null;

        try {
            statement = con.prepareStatement(SQLQueries.FIND_TOPIC_BY_ID);
            statement.setLong(1, id);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                topic = new Topic(resultSet.getLong("t_id"), resultSet.getString("name"), resultSet.getString("description"));
            }
        } catch (Exception e) {
            log.error("Can't get topic from database");
            throw new DAOException("Can't get topic from database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return Optional.ofNullable(topic);
    }

    @Override
    public Collection<Topic> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) {
        List<Topic> topics = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String temp = PaginationUtil.getEntityPaginationQuery(AttributeConstants.TOPIC_TABLE, filters);
            temp = temp.replaceFirst("\\?", sorting);
            statement = con.prepareStatement(temp);

            int k = 0;
            statement.setInt(++k, limit);
            statement.setInt(++k, offset);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                k = 0;
                topics.add(new Topic(
                        resultSet.getLong(++k),
                        resultSet.getString(++k),
                        resultSet.getString(++k)
                ));
            }
            return topics;
        } catch (Exception e) {
            log.error("Can't get all topics from database", e);
            throw new DAOException("Can't get all topics from database", e);
        } finally {
            closeAll(resultSet, statement);
        }
    }

    @Override
    public long update(Connection con, Topic topic) {
        PreparedStatement statement = null;
        long affectedRows;

        try {
            statement = con.prepareStatement(SQLQueries.UPDATE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, topic.getName());
            statement.setString(k++, topic.getDescription());
            statement.setLong(k, topic.getT_id());

            affectedRows = statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update topic");
            throw new DAOException("Can't update topic", e);
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
            statement = con.prepareStatement(SQLQueries.DELETE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't delete specified topic" + id, e);
            throw new DAOException("Can't delete specified topic", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return affectedRows;
    }

    @Override
    public long save(Connection con, Topic topic) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedId;

        try {
            statement = con.prepareStatement(SQLQueries.CREATE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, topic.getName());
            statement.setString(k, topic.getDescription());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedId = resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add topic to database", e);
            throw new DAOException("Can't add topic to database", e);
        } finally {
            closeAll(resultSet, statement);
        }

        return generatedId;
    }
}
