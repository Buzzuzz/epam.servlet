package com.servlet.ejournal.controller.commands;

import com.servlet.ejournal.constants.CommandNameConstants;
import com.servlet.ejournal.controller.commands.impl.ChangeLocaleCommand;
import com.servlet.ejournal.exceptions.NoSuchCommandException;
import com.servlet.ejournal.controller.commands.impl.course.*;
import com.servlet.ejournal.controller.commands.impl.topic.CreateTopicCommand;
import com.servlet.ejournal.controller.commands.impl.topic.DeleteTopicCommand;
import com.servlet.ejournal.controller.commands.impl.topic.GetAllTopicsCommand;
import com.servlet.ejournal.controller.commands.impl.topic.UpdateTopicCommand;
import com.servlet.ejournal.controller.commands.impl.user.*;
import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

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
        COMMANDS_POOL.put(CommandNameConstants.LOG_OUT_COMMAND, new LogOutCommand());
        COMMANDS_POOL.put(CommandNameConstants.LOG_IN_COMMAND, new LogInCommand());
        COMMANDS_POOL.put(CommandNameConstants.SIGN_UP_COMMAND, new SignUpCommand());
        COMMANDS_POOL.put(CommandNameConstants.UPDATE_USER_COMMAND, new UpdateUserCommand());
        COMMANDS_POOL.put(CommandNameConstants.GET_ALL_COURSES_COMMAND, new GetAllCoursesCommand());
        COMMANDS_POOL.put(CommandNameConstants.CHANGE_LOCALE_COMMAND, new ChangeLocaleCommand());
        COMMANDS_POOL.put(CommandNameConstants.COURSE_DETAILS_COMMAND, new GetCourseDetailsCommand());
        COMMANDS_POOL.put(CommandNameConstants.GET_ALL_TOPICS_COMMAND, new GetAllTopicsCommand());
        COMMANDS_POOL.put(CommandNameConstants.GET_ALL_USERS_COMMAND,new GetAllUsersCommand());
        COMMANDS_POOL.put(CommandNameConstants.CREATE_TOPIC, new CreateTopicCommand());
        COMMANDS_POOL.put(CommandNameConstants.UPDATE_TOPIC, new UpdateTopicCommand());
        COMMANDS_POOL.put(CommandNameConstants.DELETE_TOPIC, new DeleteTopicCommand());
        COMMANDS_POOL.put(CommandNameConstants.CHANGE_USER_LOCK_STATUS, new ChangeUserLockStatusCommand());
        COMMANDS_POOL.put(CommandNameConstants.CREATE_USER, new CreateUserCommand());
        COMMANDS_POOL.put(CommandNameConstants.DELETE_USER, new DeleteUserCommand());
        COMMANDS_POOL.put(CommandNameConstants.UPDATE_COURSE, new UpdateCourseCommand());
        COMMANDS_POOL.put(CommandNameConstants.CREATE_COURSE, new CreateCourseCommand());
        COMMANDS_POOL.put(CommandNameConstants.DELETE_COURSE, new DeleteCourseCommand());
        COMMANDS_POOL.put(CommandNameConstants.ENROLL_COMMAND, new EnrollStudentCommand());
        COMMANDS_POOL.put(CommandNameConstants.WITHDRAW_COMMAND, new WithdrawStudentCommand());
        COMMANDS_POOL.put(CommandNameConstants.COURSE_MARKS, new GetCourseMarksCommand());
        COMMANDS_POOL.put(CommandNameConstants.UPDATE_COURSE_MARKS, new UpdateCourseMarks());
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
