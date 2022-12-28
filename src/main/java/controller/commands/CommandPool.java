package controller.commands;

import controller.commands.impl.LogInCommand;
import controller.commands.impl.LogOutCommand;
import controller.commands.impl.SignUpCommand;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import static constants.CommandNameConstants.*;

/**
 * Class that contains {@link Command commands} inside of it and methods to get them
 */
@Log4j2
public class CommandPool {
    private static final Map<String, Command> COMMANDS_POOL = new HashMap<>();

    // Suppress constructor
    private CommandPool() {
    }

    // Initializing pool - adding commands
    static {
        COMMANDS_POOL.put(LOG_OUT_COMMAND, new LogOutCommand());
        COMMANDS_POOL.put(LOG_IN_COMMAND, new LogInCommand());
        COMMANDS_POOL.put(SIGN_UP_COMMAND, new SignUpCommand());

        log.debug("Initialization of CommandPool successful");
    }

    /**
     * Method that is used to get {@link Command} from {@link CommandPool} for further execution
     * @param commandName Name of command to get from {@link CommandPool}
     * @return {@link Command} instance for execution
     * @throws CommandException In case if there is no such command or any other exceptional situation
     */
    public static Command getCommand(String commandName) throws CommandException {
        try {
            Command command = COMMANDS_POOL.get(commandName);
            if (command != null) {
                log.debug("Command " + commandName + " acquired successful");
                return command;
            }
            throw new CommandException("No such command as " + commandName);
        } catch (Exception e) {
            log.error("Can't get specified command: " + commandName + " | " + e.getMessage(), e);
            throw new CommandException("Can't get specified command: " + commandName, e);
        }
    }
}