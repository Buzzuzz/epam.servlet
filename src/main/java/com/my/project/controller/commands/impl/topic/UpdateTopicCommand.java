package com.my.project.controller.commands.impl.topic;

import com.my.project.constants.AttributeConstants;
import com.my.project.constants.CommandNameConstants;
import com.my.project.controller.commands.Command;
import com.my.project.exceptions.CommandException;
import com.my.project.exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import com.my.project.services.dto.TopicDTO;
import com.my.project.services.impl.TopicServiceImpl;
import com.my.project.utils.RequestBuilder;

public class UpdateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicServiceImpl.getInstance().updateTopic(new TopicDTO(
                    Long.parseLong(req.getParameter(AttributeConstants.TOPIC_ID)),
                    req.getParameter(AttributeConstants.TOPIC_NAME_ATTR),
                    req.getParameter(AttributeConstants.TOPIC_DESCRIPTION_ATTR)
            ));
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
            throw new CommandException("Can't execute update-topic command", e);
        }
    }
}
