package controller.commands.impl.topic;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.TopicServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

public class DeleteTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicServiceImpl.getInstance().deleteTopic(Long.parseLong(req.getParameter(TOPIC_ID)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_TOPICS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            SORTING_TYPE,
                            DISPLAY_RECORDS_NUMBER,
                            CURRENT_PAGE));
        } catch (Exception e) {
            throw new CommandException("Can't execute delete-topic command", e);
        }
    }
}
