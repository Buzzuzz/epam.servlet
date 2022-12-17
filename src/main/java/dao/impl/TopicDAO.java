package dao.impl;

import static dao.ConnectionUtils.*;

import dao.*;
import entities.Topic;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * TopicDAO class is implementation of {@link DAO} interface for Topic table.
 * (Topic entity specified).
 */
@Log4j2
public class TopicDAO implements DAO<Topic> {
    @Override
    public Optional<Topic> get(long id) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Topic topic = null;

        try {
            con = DataSource.getConnection();
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
            closeAll(resultSet, statement, con);
        }

        return Optional.ofNullable(topic);
    }

    @Override
    public Collection<Topic> getAll() {
        List<Topic> topics = new ArrayList<>();
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            con = DataSource.getConnection();
            statement = con.prepareStatement(SQLQueries.FIND_ALL_TOPICS_IDS);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                get(resultSet.getLong("t_id")).ifPresent(topics::add);
            }
        } catch (Exception e) {
            log.error("Can't get all topics from database");
            throw new DAOException("Can't get all topics from database", e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return topics;
    }

    @Override
    public long update(Topic topic) {
        Connection con = null;
        PreparedStatement statement = null;
        long affectedRows;

        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(SQLQueries.UPDATE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, topic.getName());
            statement.setString(k++, topic.getDescription());
            statement.setLong(k, topic.getT_id());

            affectedRows = statement.executeUpdate();
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't update topic");
            throw new DAOException("Can't update topic", e);
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
            statement = con.prepareStatement(SQLQueries.DELETE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            statement.setLong(1, id);
            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            affectedRows = resultSet.getLong(1);
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't delete specified topic" + id, e);
            throw new DAOException("Can't delete specified topic", e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return affectedRows;
    }

    @Override
    public long save(Topic topic) {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long generatedId;

        try {
            con = DataSource.getConnection();
            con.setAutoCommit(false);
            statement = con.prepareStatement(SQLQueries.CREATE_TOPIC, Statement.RETURN_GENERATED_KEYS);

            int k = 1;
            statement.setString(k++, topic.getName());
            statement.setString(k, topic.getDescription());

            statement.executeUpdate();
            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            generatedId = resultSet.getLong(1);
            con.commit();
        } catch (Exception e) {
            rollback(con);
            log.error("Can't add topic to database");
            throw new DAOException("Can't add topic to database", e);
        } finally {
            closeAll(resultSet, statement, con);
        }

        return generatedId;
    }
}
