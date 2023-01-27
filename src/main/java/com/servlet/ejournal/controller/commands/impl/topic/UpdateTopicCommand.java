package com.servlet.ejournal.controller.commands.impl.topic;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.context.ApplicationContext;
import com.servlet.ejournal.controller.commands.Command;
import com.servlet.ejournal.exceptions.CommandException;
import com.servlet.ejournal.services.TopicService;
import jakarta.servlet.http.HttpServletRequest;
import com.servlet.ejournal.services.dto.TopicDTO;
import com.servlet.ejournal.utils.RequestBuilder;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.AttributeConstants.*;

@Log4j2
public class UpdateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        ApplicationContext context = (ApplicationContext) req.getServletContext().getAttribute(APPLICATION_CONTEXT);
        TopicService service = context.getTopicService();
        try {
            service.updateTopic(new TopicDTO(
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
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommandException("Can't execute update-topic command", e);
        }
    }
}
