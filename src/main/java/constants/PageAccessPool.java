package constants;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import model.entities.User;

import java.util.HashSet;
import java.util.Set;

import static constants.PageConstants.*;

@Log4j2
public class PageAccessPool {
    // Suppress constructor
    private PageAccessPool() {
    }

    private static final Set<String> BASE_PAGES = new HashSet<>();
    private static final Set<String> LOGGED_IN_PAGES = new HashSet<>();
    private static final Set<String> ANON_PAGES = new HashSet<>();
    private static final Set<String> STUDENT_PAGES = new HashSet<>();
    private static final Set<String> TEACHER_PAGES = new HashSet<>();
    private static final Set<String> ADMIN_PAGES = new HashSet<>();

    static {
        // base pages
        BASE_PAGES.add(HOME_PAGE);
        BASE_PAGES.add(ERROR_PAGE);
        BASE_PAGES.add(CONTROLLER);

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

    public static Set<String> getAllowedPages(HttpServletRequest req) {
        if (req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR) == null) {
            return ANON_PAGES;
        }
        User user = (User) req.getSession().getAttribute(AttributeConstants.LOGGED_USER_ATTR);
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
