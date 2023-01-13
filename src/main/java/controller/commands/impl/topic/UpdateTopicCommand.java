package controller.commands.impl.topic;

import constants.CommandNameConstants;
import controller.commands.Command;
import exceptions.CommandException;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import services.impl.TopicServiceImpl;
import utils.RequestBuilder;

import java.util.HashMap;

public class UpdateTopicCommand implements Command {
    @Override
    public String execute(HttpServletRequest req) throws CommandException {
        try {
            TopicServiceImpl.getInstance().updateTopic(req);
            return RequestBuilder.buildCommand(req.getServletPath(), CommandNameConstants.GET_ALL_TOPICS_COMMAND, new HashMap<>());
        } catch (ServiceException e) {
            throw new CommandException("Can't execute update-topic command", e);
        }
    }
}