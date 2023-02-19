package com.servlet.ejournal.services.impl;

import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.exceptions.*;
import com.servlet.ejournal.model.dao.HikariDataSource;
import com.servlet.ejournal.model.dao.interfaces.DAO;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.dto.TopicDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;
import static com.servlet.ejournal.services.TopicService.*;

@Log4j2
@Getter
public class TopicServiceImpl implements TopicService {
    private static TopicService instance;
    private final DAO<Topic> dao;
    private final HikariDataSource source;

    private TopicServiceImpl(ApplicationContext context) {
        this.source = context.getDataSource();
        this.dao = context.getTopicDAO();
    }

    public static synchronized TopicService getInstance(ApplicationContext context) {
        if (instance == null) {
            instance = new TopicServiceImpl(context);
        }
        return instance;
    }

    @Override
    public List<TopicDTO> getAllTopics(int limit, int offset, String sorting) {
        try (Connection con = source.getConnection()) {
            return dao
                    .getAll(con, limit, offset, sorting, null)
                    .stream()
                    .map(TopicService::getTopicDTO)
                    .collect(Collectors.toList());
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return getAllTopics(getTopicCount(), 0, DEFAULT_TOPIC_SORTING);
    }

    @Override
    public long createTopic(TopicDTO topicDTO) throws ServiceException {
        try (Connection con = source.getConnection()) {
            Topic topic = getTopicFromDTO(topicDTO);
            if (topic.getName() != null && topic.getDescription() != null) {
                return dao.save(con, topic);
            }
            log.error("No required parameters: " +
                    TOPIC_NAME_ATTR + ", " + TOPIC_DESCRIPTION_ATTR);
            throw new ServiceException("No required parameters for command!");
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException("Database connection error!", e);
        }
    }

    @Override
    public long updateTopic(TopicDTO topicDTO) throws ServiceException {
        Topic topic = getTopicFromDTO(topicDTO);
        try (Connection con = source.getConnection()) {
            if (topic.getName() != null && topic.getDescription() != null) {
                return dao.update(con, new Topic(
                        topic.getT_id(),
                        topic.getName(),
                        topic.getDescription()
                ));
            }
            throw new ServiceException("One of the parameters is null!");
        } catch (DAOException | SQLException e) {
            log.error("Can't update topic: " + topic.getT_id(), e);
            throw new ServiceException("Can't update topic: " + topic.getT_id(), e);
        }
    }

    @Override
    public long deleteTopic(long id) throws ServiceException {
        try (Connection con = source.getConnection()) {
            return dao.delete(con, id);
        } catch (Exception e) {
            log.error("Can't delete specified topic: " + id);
            throw new ServiceException("Can't delete specified topic: " + id);
        }
    }

    @Override
    public int getTopicCount() {
        try (Connection con = source.getConnection()) {
            return getRecordsCount(con, TOPIC_ID, TOPIC_TABLE, null);
        } catch (DAOException | SQLException e) {
            log.error(e.getMessage(), e);
            return -1;
        }
    }
}
