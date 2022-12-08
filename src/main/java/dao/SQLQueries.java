package dao;

/**
 * Class only for SQL queries in constant Strings (nothing more to see here)
 */
public class SQLQueries {
    public static final String FIND_USER_BY_ID = "select * from epam.user where u_id = ?";
    public static final String FIND_USER_BY_EMAIL = "select * from epam.user where email = ?";
    public static final String FIND_ALL_USERS_IDS = "select u_id from epam.user";
    public static final String CREATE_USER =
            "insert into epam.user(email, password, first_name, last_name, phone, user_type, is_blocked, send_notification) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_USER =
            "update epam.user set (email, password, first_name, last_name, phone, user_type, is_blocked, send_notification) = " +
                    "(?, ?, ?, ?, ?, ?, ?, ?) where u_id = ?";
    public static final String DELETE_USER = "delete from epam.users where u_id = ?";
}
