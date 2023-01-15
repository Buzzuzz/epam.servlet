package controller.commands.impl.topic;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.dto.TopicDTO;
import services.impl.TopicServiceImpl;
import utils.RequestBuilder;

import static constants.AttributeConstants.*;

@Log4j2
public class CreateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            long generatedId = TopicServiceImpl.getInstance().createTopic(new TopicDTO(
                    0,
                    req.getParameter(TOPIC_NAME_ATTR),
                    req.getParameter(TOPIC_DESCRIPTION_ATTR)
                    ));
            log.debug(String.format("Topic with id %s created successfully!", generatedId));
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
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
