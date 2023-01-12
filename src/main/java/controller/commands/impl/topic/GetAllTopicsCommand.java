package controller.commands.impl.topic;

import constants.AttributeConstants;
import constants.PageConstants;
import controller.commands.Command;
import exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.TopicServiceImpl;

public class GetAllTopicsCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        req.setAttribute(AttributeConstants.TOPICS_ATTR, TopicServiceImpl.getInstance().getAllTopics());
        return PageConstants.TOPICS_PAGE;
    }
}
