package services;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.Topic;
import services.dto.TopicDTO;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    List<TopicDTO> getAllTopics(int limit, int[] pages, int currentPage, int offset, String sorting);

    long createTopic(TopicDTO topicDTO) throws ServiceException;

    long updateTopic(TopicDTO topicDTO) throws ServiceException;

    long deleteTopic(long id) throws ServiceException;

    Topic getTopicFromDTO(TopicDTO topicDTO);

    TopicDTO getTopicDTO(Topic topic);

    int getTopicCount();
}
