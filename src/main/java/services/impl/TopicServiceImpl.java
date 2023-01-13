package services.impl;

import exceptions.ServiceException;
import exceptions.UtilException;
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
import static utils.PaginationUtil.*;

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

    // TODO: refactor getAll method
    @Override
    public List<TopicDTO> getAllTopics(HttpServletRequest req) throws ServiceException{
        Connection con = null;
        try {
            con = getConnection();
            int limit = getLimit(req);
            int[] pages = getPages(limit, getTopicCount());
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);
            String sorting = getSortingType(req, Topic.class);

            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            log.info(currentPage);
            log.info(pages.length);
            log.info(currentPage < pages.length);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, pages);

            return dao
                    .getAll(con, limit, offset, sorting)
                    .stream()
                    .map(this::getTopicDTO)
                    .collect(Collectors.toList());
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
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
    public long createTopic(HttpServletRequest req) throws ServiceException {
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

    @Override
    public long updateTopic(HttpServletRequest req) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return dao.update(con, new Topic(
                    Long.parseLong(req.getParameter(TOPIC_ID)),
                    req.getParameter(TOPIC_NAME_ATTR),
                    req.getParameter(TOPIC_DESCRIPTION_ATTR)
            ));
        } catch (Exception e) {
            log.error("Can't update topic: " + req.getParameter(TOPIC_ID), e);
            throw new ServiceException("Can't update topic: " + req.getParameter(TOPIC_ID), e);
        } finally {
            close(con);
        }
    }

    @Override
    public long deleteTopic(HttpServletRequest req) throws ServiceException {
        Connection con = null;
        try {
            con = getConnection();
            return dao.delete(con, Long.parseLong(req.getParameter(TOPIC_ID)));
        } catch (Exception e) {
            log.error("Can't delete specified topic: " + req.getParameter(TOPIC_ID));
            throw new ServiceException("Can't delete specified topic: " + req.getParameter(TOPIC_ID));
        } finally {
            close(con);
        }
    }

    @Override
    public int getTopicCount() {
        Connection con = null;
        try {
            con = getConnection();
            return getRecordsCount(con, TOPIC_ID, TOPIC_TABLE);
        } finally {
            close(con);
        }
    }
}
