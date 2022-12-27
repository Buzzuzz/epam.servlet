package controller.commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Interface to mark class as Command with only one method {@link Command#execute(HttpServletRequest)}. <br>
 * Command is a part of Command pattern, where every action is represented as object with method to do some
 * actions.
 */
@FunctionalInterface
public interface Command {
    /**
     * Method to execute command
     * @param req {@link HttpServletRequest} on which to do some actions
     * @return {@link String} to where commit redirect after successful execution of command
     * @throws CommandException If command can not be executed properly
     */
    String execute(HttpServletRequest req) throws CommandException;
}
