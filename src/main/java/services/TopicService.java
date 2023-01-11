package services;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.Topic;
import services.dto.TopicDTO;

import java.util.List;
import java.util.Optional;

public interface TopicService {
    List<TopicDTO> getAllTopics();

    TopicDTO getTopicDTO(Topic topic);
    long createTopic(HttpServletRequest req) throws ServiceException;
    long updateTopic(HttpServletRequest req) throws ServiceException;
    long deleteTopic(HttpServletRequest req) throws ServiceException;
}
