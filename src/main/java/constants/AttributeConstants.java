package constants;

public interface AttributeConstants {
    String NONE_ATTR = "none";
    String COMMAND_ATTR = "command";
    String LOGGED_USER_ATTR = "loggedUser";
    String USER_TYPE_ATTR = "userType";
    String USER_TYPE_DB = "user_type";
    String EMAIL_ATTR = "email";
    String PASSWORD_ATTR = "password";
    String PASSWORD_REPEAT_ATTR = "password-repeat";
    String LOCALE_ATTR = "locale";
    String LOCALE_EN = "en";
    String ENCODING_ATTR = "encoding";
    String ENCODING_UTF_8 = "UTF-8";
    String FIRST_NAME = "f-name";
    String LAST_NAME = "l-name";
    String PHONE_NUMBER = "phone-number";
    String ERROR = "error";
    String COURSES_ATTR = "courses";
    String PREVIOUS_REQUEST = "prevRequest";
    String COURSE_ID = "c_id";
    String COURSE_DTO = "course";
    String USERS_ATTR = "users";
    String TOPICS_ATTR = "topics";
    String TOPIC_NAME_ATTR = "topicName";
    String TOPIC_DESCRIPTION_ATTR = "topicDescription";
    String TOPIC_ID = "t_id";
    String TOPIC_TABLE = "epam.topic";
    String USER_ID = "u_id";
    String USER_STATUS = "user-status";
    String USER_TYPES = "types";
    String USER_TABLE = "epam.user";
    String RECORDS = "records";
    String DISPLAY_RECORDS_NUMBER = "display";
    String CURRENT_PAGE = "page";
    String SORTING_TYPE = "sorting";
    int DEFAULT_LIMIT = 5;
    int DEFAULT_OFFSET = 0;
    int DEFAULT_PAGE = 1;

    // If sorting field is not used, it doesn't mean it won't be
    // Please don't touch them
    String DEFAULT_USER_SORTING = "u_id";
    String DEFAULT_TOPIC_SORTING = "t_id";
}
