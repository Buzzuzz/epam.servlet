package constants;

/**
 * Interface only for SQL queries in constant Strings (nothing more to see here)
 */
public interface SQLQueries {
    String FIND_USER_BY_ID = "select * from epam.user where u_id = ?";
    String FIND_USER_BY_EMAIL = "select * from epam.user where email = ?";
    String FIND_ALL_USERS_IDS = "select u_id from epam.user";
    String FIND_USERS_PAGINATE = "select * from epam.user order by ? limit ? offset ?";
    String CREATE_USER =
            "insert into epam.user(email, password, first_name, last_name, phone, user_type, is_blocked, send_notification) " +
                    "values (?, ?, ?, ?, ?, ?, ?, ?)";
    String UPDATE_USER =
            "update epam.user set email = ?," +
                    " password = ?," +
                    " first_name = ?," +
                    " last_name = ?," +
                    " phone = ?," +
                    " user_type = ?," +
                    " is_blocked = ?," +
                    " send_notification = ?" +
                    " where u_id = ? returning u_id";
    String DELETE_USER = "delete from epam.user where u_id = ? returning u_id";
    String FIND_TOPIC_BY_ID = "select * from epam.topic where t_id = ?";
    String FIND_ALL_TOPICS_IDS = "select t_id from epam.topic";
    String DELETE_TOPIC = "delete from epam.topic where t_id = ? returning t_id";
    String CREATE_TOPIC = "insert into epam.topic(name, description) values (?, ?)";
    String UPDATE_TOPIC = "update epam.topic set name = ?, description = ? where t_id = ?";
    String FIND_COURSE_BY_ID = "select * from epam.course where c_id = ?";
    String FIND_ALL_COURSES_IDS = "select c_id from epam.course";
    String UPDATE_COURSE = "update epam.course set name = ?, description = ?, start_date = ?, end_date = ? where c_id = ?";
    String DELETE_COURSE = "delete from epam.course where c_id = ? returning c_id";
    String CREATE_COURSE = "insert into epam.course (name, description, start_date, end_date) values (?, ?, ?, ?)";
    String FIND_USER_COURSE_BY_C_ID = "select * from epam.user_course where c_id = ?";
    String UPDATE_USER_COURSE = "update epam.user_course set u_id = ?, c_id = ?, registration_date = ?, final_mark = ? where u_c_id = ?";
    String DELETE_USER_COURSE = "delete from epam.user_course where u_c_id = ? returning u_c_id";
    String CREATE_USER_COURSE = "insert into epam.user_course (u_id, c_id, registration_date, final_mark) values (?, ?, ?, ?)";
    String FIND_TOPIC_COURSE_BY_C_ID = "select * from epam.topic_course where c_id = ?";
    String UPDATE_TOPIC_COURSE = "update epam.topic_course set t_id = ?, c_id = ? where c_id = ?";
    String DELETE_TOPIC_COURSE = "delete from epam.topic_course where c_id = ? returning c_id";
    String CREATE_TOPIC_COURSE = "insert into epam.topic_course (t_id, c_id) values (?, ?)";
    String COUNT_RECORDS = "select count(?) from ?";
}
