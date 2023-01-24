package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.PageConstants;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.TopicService;

import static com.servlet.ejournal.utils.SqlUtil.*;
import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class GetAllTopicsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        TopicService service = TopicServiceImpl.getInstance();
        int limit = getLimit(req);
        int[] pages = getPages(limit, service.getTopicCount());
        int currentPage = Math.min(getCurrentPage(req), pages.length);
        int offset = getOffset(limit, currentPage);
        String sorting = getSortingType(req, DEFAULT_TOPIC_SORTING);

        req.setAttribute(TOPICS_ATTR, service.getAllTopics(limit, offset, sorting));
        req.setAttribute(SORTING_TYPE, sorting);
        req.setAttribute(DISPLAY_RECORDS_NUMBER, limit);
        req.setAttribute(CURRENT_PAGE, currentPage);
        req.setAttribute(RECORDS, pages);

        return PageConstants.TOPICS_PAGE;
    }
}
