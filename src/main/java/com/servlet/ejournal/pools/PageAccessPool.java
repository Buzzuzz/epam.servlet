package com.servlet.ejournal.pools;

import com.servlet.ejournal.constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import com.servlet.ejournal.model.entities.User;

import java.util.HashSet;
import java.util.Set;

import static com.servlet.ejournal.constants.PageConstants.*;

@Log4j2
public class PageAccessPool implements Pool<String, User> {
    // Suppress constructor
    private PageAccessPool() {
    }

    private static class Holder {
        private static final Pool<String, User> pool = new PageAccessPool();
    }

    public static Pool<String, User> getInstance() {
        return Holder.pool;
    }

    private static final Set<String> BASE_PAGES = new HashSet<>();
    private static final Set<String> ANON_PAGES = new HashSet<>();
    private static final Set<String> LOGGED_IN_PAGES = new HashSet<>();
    private static final Set<String> STUDENT_PAGES = new HashSet<>();
    private static final Set<String> TEACHER_PAGES = new HashSet<>();
    private static final Set<String> ADMIN_PAGES = new HashSet<>();

    static {
        // base pages
        BASE_PAGES.add(HOME_PAGE);
        BASE_PAGES.add(ERROR_PAGE);
        BASE_PAGES.add(CONTROLLER_MAPPING);

        // logged in pages
        LOGGED_IN_PAGES.addAll(BASE_PAGES);
        LOGGED_IN_PAGES.add(CABINET_PAGE);
        LOGGED_IN_PAGES.add(COURSES_PAGE);
        LOGGED_IN_PAGES.add(COURSE_DETAILS_PAGE);

        // anon pages
        ANON_PAGES.addAll(BASE_PAGES);
        ANON_PAGES.add(LOGIN_PAGE);
        ANON_PAGES.add(SIGN_UP_PAGE);
        ANON_PAGES.add(RESTORE_PASSWORD_PAGE);

        // student pages
        STUDENT_PAGES.addAll(BASE_PAGES);
        STUDENT_PAGES.addAll(LOGGED_IN_PAGES);

        // teacher pages
        TEACHER_PAGES.addAll(BASE_PAGES);
        TEACHER_PAGES.addAll(LOGGED_IN_PAGES);
        TEACHER_PAGES.add(MARKS_PAGE);

        // admin pages
        ADMIN_PAGES.addAll(BASE_PAGES);
        ADMIN_PAGES.addAll(LOGGED_IN_PAGES);
        ADMIN_PAGES.add(TOPICS_PAGE);
        ADMIN_PAGES.add(USERS_PAGE);

        log.debug("Access commands initialized successful");
    }

    @Override
    public Set<String> getAllowedInstances(User user) {
        if (user == null) {
            return ANON_PAGES;
        }
        switch (user.getUser_type()) {
            case STUDENT:
                return STUDENT_PAGES;
            case TEACHER:
                return TEACHER_PAGES;
            case ADMINISTRATOR:
                return ADMIN_PAGES;
            default:
                return ANON_PAGES;
        }
    }
}
