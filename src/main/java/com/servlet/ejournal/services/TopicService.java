package com.servlet.ejournal.services;

import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.model.entities.Topic;

import java.util.List;

public interface TopicService {
    List<TopicDTO> getAllTopics(int limit, int offset, String sorting);

    List<TopicDTO> getAllTopics();

    long createTopic(TopicDTO topicDTO) throws ServiceException;

    long updateTopic(TopicDTO topicDTO) throws ServiceException;

    long deleteTopic(long id) throws ServiceException;

    static Topic getTopicFromDTO(TopicDTO topicDTO) {
        return new Topic(
                topicDTO.getTopicId(),
                topicDTO.getTopicName(),
                topicDTO.getTopicDescription()
        );
    }

    static TopicDTO getTopicDTO(Topic topic) {
        return new TopicDTO(
                topic.getT_id(),
                topic.getName(),
                topic.getDescription()
        );
    }

    int getTopicCount();
}
