package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

@Log4j2
public class CreateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            long generatedId = TopicServiceImpl.getInstance().createTopic(new TopicDTO(
                    0,
                    req.getParameter(AttributeConstants.TOPIC_NAME_ATTR),
                    req.getParameter(AttributeConstants.TOPIC_DESCRIPTION_ATTR)
                    ));
            log.debug(String.format("Topic with id %s created successfully!", generatedId));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_TOPICS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            AttributeConstants.SORTING_TYPE,
                            AttributeConstants.DISPLAY_RECORDS_NUMBER,
                            AttributeConstants.CURRENT_PAGE
                    ));
        } catch (ServiceException e) {
            log.error(e.getMessage(), e);
            throw new CommandException(e.getMessage(), e);
        }
    }
}
