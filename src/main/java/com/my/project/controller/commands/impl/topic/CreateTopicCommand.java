package com.my.project.controller.commands.impl.topic;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.my.project.services.dto.TopicDTO;
import com.my.project.services.impl.TopicServiceImpl;
import com.my.project.utils.RequestBuilder;

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
