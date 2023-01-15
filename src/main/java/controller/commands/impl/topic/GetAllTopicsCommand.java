package controller.commands.impl.topic;

import constants.AttributeConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.Topic;
import services.TopicService;
import services.impl.TopicServiceImpl;

import static constants.AttributeConstants.*;
import static utils.PaginationUtil.*;

@Log4j2
public class GetAllTopicsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicService service = TopicServiceImpl.getInstance();
            int limit = getLimit(req);
            int[] pages = getPages(limit, service.getTopicCount());
            int currentPage = Math.min(getCurrentPage(req), pages.length);
            int offset = getOffset(limit, currentPage);
            String sorting = getSortingType(req, Topic.class);

            req.setAttribute(TOPICS_ATTR, service.getAllTopics(limit, pages, currentPage, offset, sorting));
            req.setAttribute(SORTING_TYPE, sorting);
            req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(CURRENT_PAGE, currentPage);
            req.setAttribute(RECORDS, pages);

            return PageConstants.TOPICS_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
