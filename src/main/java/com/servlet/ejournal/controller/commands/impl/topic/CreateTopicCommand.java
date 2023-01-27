package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import com.servlet.ejournal.services.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class CreateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        TopicService service = context.getTopicService();
        try {
            long generatedId = service.createTopic(new TopicDTO(0, req.getParameter(TOPIC_NAME_ATTR), req.getParameter(TOPIC_DESCRIPTION_ATTR)));
            log.debug(String.format("Topic with id %s created successfully!", generatedId));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_TOPICS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(req.getParameterMap(), SORTING_TYPE, DISPLAY_RECORDS_NUMBER, CURRENT_PAGE));
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
