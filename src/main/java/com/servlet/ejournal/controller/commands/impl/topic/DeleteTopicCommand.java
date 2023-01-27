package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.services.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.services.impl.TopicServiceImpl;
import com.servlet.ejournal.utils.RequestBuilder;

import static com.servlet.ejournal.constants.AttributeConstants.*;

public class DeleteTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        TopicService service = context.getTopicService();
        try {
            service.deleteTopic(Long.parseLong(req.getParameter(TOPIC_ID)));
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
