package com.servlet.ejournal.constants;

public interface AttributeConstants {
    // General
    String CONTROLLER_ATTR = "controller";
    String APPLICATION_CONTEXT = "appContext";
    String PREVIOUS_REQUEST = "prevRequest";
    String ERROR_ATTR = "error";
    String NONE_ATTR = "none";
    String COMMAND_ATTR = "command";
    String LOCALE_ATTR = "locale";
    String LOCALE_EN = "en";
    String ENCODING_ATTR = "encoding";
    String ENCODING_UTF_8 = "UTF-8";
    String DB_ERROR = "db-error";
    String SWITCH = "switch";
    String QUERY = "query";
    String FULL_COLUMN_NAME = "%s.%s";

    // Forms from View layer
    String FIRST_NAME = "f-name";
    String LAST_NAME = "l-name";
    String PHONE_NUMBER = "phone-number";
    String EMAIL_ATTR = "email";
    String PASSWORD_ATTR = "password";
    String PASSWORD_REPEAT_ATTR = "password-repeat";

    // User
    String USER_ID = "u_id";
    String USER_TYPE_ATTR = "userType";
    String USER_TYPE_DB = "user_type";
    String USER_TYPES = "types";
    String USER_STATUS = "user-status";
    String USERS_ATTR = "users";
    String LOGGED_USER_ATTR = "loggedUser";
    String USER_TABLE = "epam.user";

    // Topic
    String TOPICS_ATTR = "topics";
    String TOPIC_NAME_ATTR = "topicName";
    String TOPIC_DESCRIPTION_ATTR = "topicDescription";
    String TOPIC_ID = "t_id";
    String TOPIC_TABLE = "epam.topic";

    // Course
    String COURSES_ATTR = "courses";
    String COURSE_ID = "c_id";
    String COURSE_TABLE = "epam.course";
    String COURSE_NAME = "courseName";
    String COURSE_DESCRIPTION = "courseDescription";
    String COURSE_DTO = "course";
    String COURSE_START_DATE = "startDate";
    String COURSE_END_DATE = "endDate";
    String ENROLLED_ASC_SORTING = "enroll-asc";
    String ENROLLED_DESC_SORTING = "enroll-desc";
    String COURSE_NOT_STARTED = "not-started";
    String COURSE_IN_PROGRESS = "in-progress";
    String COURSE_ENDED = "ended";
    String END_DATE_FILTER = "endDate_filter";
    String FINAL_MARK = "final_mark";

    // TeacherCourse
    String TEACHER_ID = "tch_id";
    String TEACHER_COURSE_TABLE = "epam.teacher_course";
    String TEACHERS_ATTR = "teachers";

    // Pagination
    String RECORDS = "records";
    String DISPLAY_RECORDS_NUMBER = "display";
    String CURRENT_PAGE = "page";
    String SORTING_TYPE = "sorting";
    int DEFAULT_LIMIT = 5;
    int DEFAULT_OFFSET = 0;
    int DEFAULT_PAGE = 1;

    // SQL
    String USER_COURSE_TABLE = "epam.user_course";
    String TOPIC_COURSE_TABLE = "epam.topic_course";

    // Sorting
    // If sorting field is not used, it doesn't mean it won't be
    // Please don't touch them
    String DEFAULT_USER_SORTING = "u_id";
    String DEFAULT_TOPIC_SORTING = "t_id";
    String DEFAULT_COURSE_SORTING = "c_id";
}
