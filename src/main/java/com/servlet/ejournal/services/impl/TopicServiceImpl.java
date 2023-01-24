package com.servlet.ejournal.services.impl;

import com.servlet.ejournal.exceptions.*;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.dao.impl.TopicDAO;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.services.TopicService;
import com.servlet.ejournal.services.dto.TopicDTO;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import static com.servlet.ejournal.model.dao.DataSource.*;
import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
@Getter
public class TopicServiceImpl implements TopicService {
    private TopicDAO dao = TopicDAO.getInstance();

    // Suppress constructor
    private TopicServiceImpl() {
    }

    private static class Holder {
        private static final TopicServiceImpl service = new TopicServiceImpl();
    }

    public static TopicServiceImpl getInstance() {
        return Holder.service;
    }

    @Override
    public List<TopicDTO> getAllTopics(int limit, int offset, String sorting) {
        Connection con = null;
        try {
            con = getConnection();
            return dao
                    .getAll(con, limit, offset, sorting, null)
                    .stream()
                    .map(this::getTopicDTO)
                    .collect(Collectors.toList());
        } finally {
            close(con);
        }
    }

    @Override
    public List<TopicDTO> getAllTopics() {
        return getAllTopics(getTopicCount(), 0, DEFAULT_TOPIC_SORTING);
    }

    @Override
    public TopicDTO getTopicDTO(Topic topic) {
        return new TopicDTO(
                topic.getT_id(),
                topic.getName(),
                topic.getDescription()
        );
    }

    @Override
    public long createTopic(TopicDTO topicDTO) throws ServiceException {
        Connection con = null;
        try {
            Topic topic = getTopicFromDTO(topicDTO);
            if (topic.getName() != null && topic.getDescription() != null) {
                con = getConnection();
                return dao.save(con, topic);
            }
            log.error("No required parameters: " +
                    TOPIC_NAME_ATTR + ", " + TOPIC_DESCRIPTION_ATTR);
            throw new ServiceException("No required parameters for command!");
        } finally {
            close(con);
        }
    }

    @Override
    public long updateTopic(TopicDTO topicDTO) throws ServiceException {
        Connection con = null;
        Topic topic = getTopicFromDTO(topicDTO);
        try {
            if (topic.getName() != null && topic.getDescription() != null) {
                con = getConnection();

                return dao.update(con, new Topic(
                        topic.getT_id(),
                        topic.getName(),
                        topic.getDescription()
                ));
            }
            return -1;
        } catch (DAOException e) {
            log.error("Can't update topic: " + topic.getT_id(), e);
            throw new ServiceException("Can't update topic: " + topic.getT_id(), e);
        } finally {
            close(con);
        }
    }

    @Override
    public long deleteTopic(long id) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return dao.delete(con, id);
        } catch (Exception e) {
            log.error("Can't delete specified topic: " + id);
            throw new ServiceException("Can't delete specified topic: " + id);
        } finally {
            close(con);
        }
    }

    @Override
    public Topic getTopicFromDTO(TopicDTO topicDTO) {
        return new Topic(
                topicDTO.getTopicId(),
                topicDTO.getTopicName(),
                topicDTO.getTopicDescription()
        );
    }

    @Override
    public int getTopicCount() {
        Connection con = null;
        try {
            con = getConnection();
            return getRecordsCount(con, TOPIC_ID, TOPIC_TABLE, null);
        } finally {
            close(con);
        }
    }
}
