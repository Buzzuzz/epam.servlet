package controller.commands.impl.topic;

import constants.AttributeConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.impl.TopicServiceImpl;

import static constants.AttributeConstants.*;

@Log4j2
public class GetAllTopicsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            req.setAttribute(TOPICS_ATTR, TopicServiceImpl.getInstance().getAllTopics(req));
            req.setAttribute(RECORDS, req.getAttribute(RECORDS));
            req.setAttribute(SORTING_TYPE, req.getAttribute(SORTING_TYPE));
            req.setAttribute(DISPLAY_RECORDS_NUMBER, req.getAttribute(DISPLAY_RECORDS_NUMBER));
            req.setAttribute(CURRENT_PAGE, req.getAttribute(CURRENT_PAGE));
            return PageConstants.TOPICS_PAGE;
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
