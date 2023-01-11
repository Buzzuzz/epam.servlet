package services.impl;

import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.dao.impl.TopicDAO;
import model.entities.Topic;
import services.TopicService;
import services.dto.TopicDTO;

import java.sql.Connection;
import java.util.List;
import java.util.stream.Collectors;

import static model.dao.DataSource.*;
import static constants.AttributeConstants.*;

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
    public List<TopicDTO> getAllTopics() {
        Connection con = null;
        try {
            con = getConnection();
            return dao
                    .getAll(con)
                    .stream()
                    .map(this::getTopicDTO)
                    .collect(Collectors.toList());
        } finally {
            close(con);
        }
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
    public long createTopic(HttpServletRequest req) throws ServiceException{
        Connection con = null;
        try {
            if (req.getParameter(TOPIC_NAME_ATTR) != null && req.getParameter(TOPIC_DESCRIPTION_ATTR) != null) {
                con = getConnection();
                return dao.save(con, new Topic(
                        0,
                        req.getParameter(TOPIC_NAME_ATTR),
                        req.getParameter(TOPIC_DESCRIPTION_ATTR)
                ));
            }
            log.error("No required parameters: " +
                    TOPIC_NAME_ATTR + ", " + TOPIC_DESCRIPTION_ATTR);
            throw new ServiceException("No required parameters for command!");
        } finally {
            close(con);
        }
    }
}
