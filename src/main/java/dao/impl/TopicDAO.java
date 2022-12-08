package dao.impl;

import dao.*;
import entities.Topic;
import lombok.extern.log4j.Log4j2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * TopicDAO class is implementation of {@link DAO} interface for Topic table. (Topic entity specified).
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
            ConnectionUtils.close(resultSet);
            ConnectionUtils.close(statement);
            ConnectionUtils.close(con);
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
            ConnectionUtils.close(resultSet);
            ConnectionUtils.close(statement);
            ConnectionUtils.close(con);
        }

        return topics;
    }

    @Override
    public long update(Topic entity) {
        return 0;
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
            resultSet = statement.executeQuery();
            resultSet.next();
            affectedRows = resultSet.getLong("t_id");
            con.commit();
        } catch (Exception e) {
            ConnectionUtils.rollback(con);
            log.error("Can't delete specified topic");
            throw new DAOException("Can't delete specified topic", e);
        } finally {
            ConnectionUtils.close(resultSet);
            ConnectionUtils.close(statement);
            ConnectionUtils.close(con);
        }

        return affectedRows;
    }

    @Override
    public long save(Topic entity) {
        return 0;
    }
}
