package controller.commands;

import jakarta.servlet.http.HttpServletRequest;

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
     */
    String execute(HttpServletRequest req);
}
