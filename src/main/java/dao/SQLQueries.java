package dao;

/**
 * Class only for SQL queries in constant Strings (nothing more to see here)
 */
public class SQLQueries {
    // Suppress constructor
    private SQLQueries() {}
    public static final String FIND_USER_BY_ID = "select * from epam.user where u_id = ?";
    public static final String FIND_USER_BY_EMAIL = "select * from epam.user where email = ?";
    public static final String FIND_ALL_USERS_IDS = "select u_id from epam.user";
    public static final String CREATE_USER =
            "insert into epam.user(email, password, first_name, last_name, phone, user_type, is_blocked, send_notification) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USER =
            "update epam.user set email = ?," +
                    " password = ?," +
                    " first_name = ?," +
                    " last_name = ?," +
                    " phone = ?," +
                    " user_type = ?," +
                    " is_blocked = ?," +
                    " send_notification = ?" +
                    " where u_id = ? returning u_id";
    public static final String DELETE_USER = "delete from epam.user where u_id = ? returning u_id";
    public static final String FIND_TOPIC_BY_ID = "select * from epam.topic where t_id = ?";
    public static final String FIND_ALL_TOPICS_IDS = "select t_id from epam.topic";
    public static final String DELETE_TOPIC = "delete from epam.topic where t_id = ? returning t_id";
    public static final String CREATE_TOPIC = "insert into epam.topic(name, description) values (?, ?)";
    public static final String UPDATE_TOPIC = "update epam.topic set name = ?, description = ? where t_id = ?";
    public static final String FIND_COURSE_BY_ID = "select * from epam.course where c_id = ?";
    public static final String FIND_ALL_COURSES_IDS = "select c_id from epam.course";
    public static final String UPDATE_COURSE = "update epam.course set name = ?, description = ?, start_date = ?, end_date = ? where c_id = ?";
    public static final String DELETE_COURSE = "delete from epam.course where c_id = ? returning c_id";
    public static final String CREATE_COURSE = "insert into epam.course (name, description, start_date, end_date) values (?, ?, ?, ?)";
}
