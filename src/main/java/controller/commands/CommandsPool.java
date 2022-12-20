package controller.commands;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains {@link Command commands} inside of it and methods to get them
 */
@Log4j2
public class CommandsPool {
    private static final Map<String, Command> COMMANDS_POOL = new HashMap<>();

    // Suppress constructor
    private CommandsPool() {
    }

    // Initializing pool - adding commands
    static {
        log.debug("Started CommandPool initialization");
        //TODO: instantiate command objects here
        log.debug("Initialization of CommandPool successful");
    }

    /**
     * Method that is used to get {@link Command} from {@link CommandsPool} for further execution
     * @param commandName Name of command to get from {@link CommandsPool}
     * @return {@link Command} instance for execution
     * @throws CommandException In case if there is no such command or any other exceptional situation
     */
    public static Command getCommand(String commandName) throws CommandException {
        try {
            log.debug("Started command extraction");
            Command command = COMMANDS_POOL.get(commandName);
            if (command != null) {
                log.debug("Command " + commandName + " acquired successful");
                return command;
            }
            throw new CommandException("No such command as " + commandName);
        } catch (Exception e) {
            log.error("Can't get specified command: " + commandName + " | " + e.getMessage(), e);
            throw new CommandException("Can't get specified command", e);
        }
    }
}
