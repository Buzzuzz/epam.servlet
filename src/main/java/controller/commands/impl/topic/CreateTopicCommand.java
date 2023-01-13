package controller.commands.impl.topic;

import constants.AttributeConstants;
import constants.CommandNameConstants;
import constants.PageConstants;
import controller.commands.Command;
import controller.commands.CommandPool;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import services.impl.TopicServiceImpl;
import utils.RequestBuilder;

import java.util.HashMap;

@Log4j2
public class CreateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            log.debug("Topic with ID: " + TopicServiceImpl.getInstance().createTopic(req) + " " +
                    "created successfully");
            return RequestBuilder.buildCommand(req.getServletPath(), CommandNameConstants.GET_ALL_TOPICS_COMMAND, new HashMap<>());
        } catch (ServiceException e) {
            throw new CommandException("Error in createTopic command", e);
        }
    }
}
