package dao;

/**
 * Class only for SQL queries in constants Strings (nothing more to see here)
 */
public class SQLQueries {
    public static final String FIND_USER_BY_ID = "select * from epam.user where u_id = ?";
    public static final String FIND_USER_BY_EMAIL = "select * from epam.user where email = ?";
    public static final String FIND_ALL_USERS = "select * from epam.user";
    public static final String CREATE_USER =
            "insert into epam.user(email, password, first_name, last_name, phone, user_type, is_blocked, send_notification) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
}
