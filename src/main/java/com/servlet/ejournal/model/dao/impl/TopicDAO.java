package com.servlet.ejournal.model.dao.impl;

import com.servlet.ejournal.constants.SQLQueries;
import com.servlet.ejournal.exceptions.DAOException;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.utils.SqlUtil;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.*;

import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.constants.SQLQueries.*;

/**
 * TopicDAO class is implementation of {@link DAO} interface for Topic table.
 * (Topic entity specified).
 */
@Log4j2
public class TopicDAO implements DAO<Topic> {
    private static TopicDAO instance;
    private final HikariDataSource source;

    //Suppress constructor
    private TopicDAO(HikariDataSource source) {
        this.source = source;
    }

    /**
     * Method to get TopicDao instance (singleton)
     *
     * @param source - DataSource from where to acquire connections
     * @return {@link DAO} implementation for specified entity ({@link Topic} in this case)
     */
    public static synchronized TopicDAO getInstance(HikariDataSource source) {
        if (instance == null) {
            instance = new TopicDAO(source);
        }
        return instance;
    }

    @Override
    public Optional<Topic> get(Connection con, long id) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(FIND_TOPIC_BY_ID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            return resultSet.next() ? Optional.of(createTopicObject(resultSet)) : Optional.empty();
        } catch (SQLException e) {
            String message = String.format("%s %s", "Can't get topic from database, id:", id);
            log.error(message, e);
            throw new DAOException(message, e);
        } finally {
            source.close(resultSet);
        }
    }

    @Override
    public Collection<Topic> getAll(Connection con, int limit, int offset, String sorting, Map<String, String[]> filters) throws DAOException {
        List<Topic> topics = new ArrayList<>();
        String query = SqlUtil.getAllEntitiesQuery(TOPIC_TABLE, limit, offset, sorting, filters);

        try (PreparedStatement statement = con.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                topics.add(createTopicObject(resultSet));
            }

            return topics;
        } catch (SQLException e) {
            log.error("Can't get all topics from database", e);
            throw new DAOException("Can't get all topics from database", e);
        }
    }

    @Override
    public long update(Connection con, Topic topic) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(UPDATE_TOPIC, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setString(++k, topic.getName());
            statement.setString(++k, topic.getDescription());
            statement.setLong(++k, topic.getT_id());

            return statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't update topic " + topic.getT_id(), e);
            throw new DAOException("Can't update topic " + topic.getT_id(), e);
        }
    }

    @Override
    public long delete(Connection con, long id) throws DAOException {
        try (PreparedStatement statement = con.prepareStatement(DELETE_TOPIC)) {
            statement.setLong(1, id);
            return statement.executeUpdate();
        } catch (Exception e) {
            log.error("Can't delete specified topic, id: " + id, e);
            throw new DAOException("Can't delete specified topic, id: " + id, e);
        }

    }

    @Override
    public long save(Connection con, Topic topic) throws DAOException {
        ResultSet resultSet = null;

        try (PreparedStatement statement = con.prepareStatement(SQLQueries.CREATE_TOPIC, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            statement.setString(++k, topic.getName());
            statement.setString(++k, topic.getDescription());
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getLong(1);
        } catch (Exception e) {
            log.error("Can't add topic to database", e);
            throw new DAOException("Can't add topic to database", e);
        } finally {
            source.close(resultSet);
        }
    }

    private Topic createTopicObject(ResultSet resultSet) throws SQLException {
        int k = 0;
        return new Topic(resultSet.getLong(++k), resultSet.getString(++k), resultSet.getString(++k));
    }
}
