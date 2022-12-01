package entities;

import lombok.Data;

@Data
public class User {
    private final long u_id;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String phone;
    private UserType user_type;
    boolean is_blocked;
    boolean send_notification;
}
