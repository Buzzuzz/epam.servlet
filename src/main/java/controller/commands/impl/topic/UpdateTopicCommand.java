package controller.commands.impl.topic;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import services.dto.TopicDTO;
import services.impl.TopicServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

public class UpdateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicServiceImpl.getInstance().updateTopic(new TopicDTO(
                    Long.parseLong(req.getParameter(TOPIC_ID)),
                    req.getParameter(TOPIC_NAME_ATTR),
                    req.getParameter(TOPIC_DESCRIPTION_ATTR)
            ));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_TOPICS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            SORTING_TYPE,
                            DISPLAY_RECORDS_NUMBER,
                            CURRENT_PAGE
                    ));
        } catch (ServiceException e) {
            throw new CommandException("Can't execute update-topic command", e);
        }
    }
}
