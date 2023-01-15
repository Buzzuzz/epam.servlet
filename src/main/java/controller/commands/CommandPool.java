package controller.commands;

import controller.commands.impl.*;
import controller.commands.impl.course.CourseDetailsCommand;
import controller.commands.impl.course.CreateCourseCommand;
import controller.commands.impl.course.GetAllCoursesCommand;
import controller.commands.impl.course.UpdateCourseCommand;
import controller.commands.impl.topic.CreateTopicCommand;
import controller.commands.impl.topic.DeleteTopicCommand;
import controller.commands.impl.topic.GetAllTopicsCommand;
import controller.commands.impl.topic.UpdateTopicCommand;
import controller.commands.impl.user.*;
import exceptions.NoSuchCommandException;
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
        COMMANDS_POOL.put(UPDATE_USER_COMMAND, new UpdateUserCommand());
        COMMANDS_POOL.put(GET_ALL_COURSES_COMMAND, new GetAllCoursesCommand());
        COMMANDS_POOL.put(CHANGE_LOCALE_COMMAND, new ChangeLocaleCommand());
        COMMANDS_POOL.put(COURSE_DETAILS_COMMAND, new CourseDetailsCommand());
        COMMANDS_POOL.put(GET_ALL_TOPICS_COMMAND, new GetAllTopicsCommand());
        COMMANDS_POOL.put(GET_ALL_USERS_COMMAND,new GetAllUsersCommand());
        COMMANDS_POOL.put(CREATE_TOPIC, new CreateTopicCommand());
        COMMANDS_POOL.put(UPDATE_TOPIC, new UpdateTopicCommand());
        COMMANDS_POOL.put(DELETE_TOPIC, new DeleteTopicCommand());
        COMMANDS_POOL.put(CHANGE_USER_LOCK_STATUS, new ChangeUserLockStatusCommand());
        COMMANDS_POOL.put(CREATE_USER, new CreateUserCommand());
        COMMANDS_POOL.put(DELETE_USER, new DeleteUserCommand());
        COMMANDS_POOL.put(UPDATE_COURSE, new UpdateCourseCommand());
        COMMANDS_POOL.put(CREATE_COURSE, new CreateCourseCommand());

        log.debug("Initialization of CommandPool successful");
    }

    /**
     * Method that is used to get {@link Command} from {@link CommandPool} for further execution
     *
     * @param commandName Name of command to get from {@link CommandPool}
     * @return {@link Command} instance for execution
     * @throws NoSuchCommandException In case if there is no such command
     */
    public static Command getCommand(String commandName) throws NoSuchCommandException {
        Command command = COMMANDS_POOL.get(commandName);
        if (command != null) {
            log.debug("Command " + commandName + " acquired successful");
            return command;
        }
        log.error("No such command as " + commandName);
        throw new NoSuchCommandException("No such command as " + commandName);
    }
}
