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

    Topic getTopicFromDTO(TopicDTO topicDTO);

    TopicDTO getTopicDTO(Topic topic);

    int getTopicCount();
}
