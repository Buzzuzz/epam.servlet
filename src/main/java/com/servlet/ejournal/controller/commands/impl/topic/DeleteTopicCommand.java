package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.AttributeConstants;
import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

public class DeleteTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicServiceImpl.getInstance().deleteTopic(Long.parseLong(req.getParameter(AttributeConstants.TOPIC_ID)));
            return RequestBuilder.buildCommand(
                    req.getServletPath(),
                    CommandNameConstants.GET_ALL_TOPICS_COMMAND,
                    RequestBuilder.getSpecifiedParamsMap(
                            req.getParameterMap(),
                            AttributeConstants.SORTING_TYPE,
                            AttributeConstants.DISPLAY_RECORDS_NUMBER,
                            AttributeConstants.CURRENT_PAGE));
        } catch (Exception e) {
            throw new CommandException("Can't execute delete-topic command", e);
        }
    }
}
