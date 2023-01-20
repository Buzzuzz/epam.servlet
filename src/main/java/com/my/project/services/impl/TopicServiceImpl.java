package com.my.project.services.impl;

import com.my.project.constants.AttributeConstants;
import com.my.project.exceptions.DAOException;
import com.my.project.exceptions.ServiceException;
import lombok.extern.log4j.Log4j2;
import com.my.project.model.dao.impl.TopicDAO;
import com.my.project.model.entities.Topic;
import com.my.project.services.TopicService;
import com.my.project.services.dto.TopicDTO;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import static com.my.project.model.dao.DataSource.*;
import static com.my.project.utils.SqlUtil.*;

@Log4j2
public class TopicServiceImpl implements TopicService {
    private static final TopicDAO dao = TopicDAO.getInstance();

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
        return getAllTopics(getTopicCount(), 0, AttributeConstants.DEFAULT_TOPIC_SORTING);
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
                    AttributeConstants.TOPIC_NAME_ATTR + ", " + AttributeConstants.TOPIC_DESCRIPTION_ATTR);
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
            return getRecordsCount(con, AttributeConstants.TOPIC_ID, AttributeConstants.TOPIC_TABLE, null);
        } finally {
            close(con);
        }
    }
}
