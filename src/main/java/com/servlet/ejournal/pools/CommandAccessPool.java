package com.servlet.ejournal.pools;

import com.servlet.ejournal.model.entities.User;
import lombok.extern.log4j.Log4j2;

import static com.servlet.ejournal.constants.CommandNameConstants.*;

import java.util.HashSet;
import java.util.Set;

@Log4j2
public class CommandAccessPool implements Pool<String, User> {
    // Suppress constructor
    private CommandAccessPool() {
    }

    private static class Holder {
        private static final Pool<String, User> pool = new CommandAccessPool();
    }

    public static Pool<String, User> getInstance() {
        return Holder.pool;
    }

    private static final Set<String> ANON_COMMANDS = new HashSet<>();
    private static final Set<String> BASE_LOGGED_IN_COMMANDS = new HashSet<>();
    private static final Set<String> STUDENT_COMMANDS = new HashSet<>();
    private static final Set<String> TEACHER_COMMANDS = new HashSet<>();
    private static final Set<String> ADMIN_COMMANDS = new HashSet<>();

    static {
        log.trace("Command access pool initialization...");

        ANON_COMMANDS.add(LOG_OUT_COMMAND);
        ANON_COMMANDS.add(LOG_IN_COMMAND);
        ANON_COMMANDS.add(SIGN_UP_COMMAND);
        ANON_COMMANDS.add(CHANGE_LOCALE_COMMAND);

        BASE_LOGGED_IN_COMMANDS.add(LOG_OUT_COMMAND);
        BASE_LOGGED_IN_COMMANDS.add(GET_ALL_COURSES_COMMAND);
        BASE_LOGGED_IN_COMMANDS.add(UPDATE_USER_COMMAND);
        BASE_LOGGED_IN_COMMANDS.add(COURSE_DETAILS_COMMAND);
        BASE_LOGGED_IN_COMMANDS.add(CHANGE_LOCALE_COMMAND);

        STUDENT_COMMANDS.addAll(BASE_LOGGED_IN_COMMANDS);
        STUDENT_COMMANDS.add(ENROLL_COMMAND);
        STUDENT_COMMANDS.add(WITHDRAW_COMMAND);

        TEACHER_COMMANDS.addAll(BASE_LOGGED_IN_COMMANDS);
        TEACHER_COMMANDS.add(COURSE_MARKS);
        TEACHER_COMMANDS.add(UPDATE_COURSE_MARKS);

        ADMIN_COMMANDS.addAll(BASE_LOGGED_IN_COMMANDS);
        ADMIN_COMMANDS.add(CREATE_COURSE);
        ADMIN_COMMANDS.add(UPDATE_COURSE);
        ADMIN_COMMANDS.add(DELETE_COURSE);

        ADMIN_COMMANDS.add(GET_ALL_TOPICS_COMMAND);
        ADMIN_COMMANDS.add(CREATE_TOPIC);
        ADMIN_COMMANDS.add(UPDATE_TOPIC);
        ADMIN_COMMANDS.add(DELETE_TOPIC);

        ADMIN_COMMANDS.add(GET_ALL_USERS_COMMAND);
        ADMIN_COMMANDS.add(CREATE_USER);
        ADMIN_COMMANDS.add(CHANGE_USER_LOCK_STATUS);
        ADMIN_COMMANDS.add(DELETE_USER);


        log.trace("Command access pool initialization successful!");
    }

    @Override
    public Set<String> getAllowedInstances(User user) {
        if (user == null) {
            return ANON_COMMANDS;
        }
        switch (user.getUser_type()) {
            case STUDENT:
                return STUDENT_COMMANDS;
            case TEACHER:
                return TEACHER_COMMANDS;
            case ADMINISTRATOR:
                return ADMIN_COMMANDS;
            default:
                return ANON_COMMANDS;
        }
    }
}
