package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.UtilException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.Topic;
import com.servlet.ejournal.services.TopicService;

import static com.servlet.ejournal.utils.SqlUtil.*;

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

            req.setAttribute(AttributeConstants.TOPICS_ATTR, service.getAllTopics(limit, offset, sorting));
            req.setAttribute(AttributeConstants.SORTING_TYPE, sorting);
            req.setAttribute(AttributeConstants.DISPLAY_RECORDS_NUMBER, limit);
            req.setAttribute(AttributeConstants.CURRENT_PAGE, currentPage);
            req.setAttribute(AttributeConstants.RECORDS, pages);

            return PageConstants.TOPICS_PAGE;
        } catch (UtilException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
