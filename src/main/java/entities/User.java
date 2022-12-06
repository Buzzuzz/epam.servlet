package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * User entity from database.
 * All default getters/setters created via lombok. <br>
 * Check {@link UserType UserType} for possible user types
 * (System access levels depend on them).
 */
@Data @AllArgsConstructor
public class User {
    private final long u_id;
    private String email;
    @ToString.Exclude
    private String password;
    private String first_name;
    private String last_name;
    private String phone;
    private UserType user_type;
    boolean is_blocked;
    boolean send_notification;

    /**
     * This constructor is needed to create user with ID only without possibility to change it.
     * @param ID For assigning to user object
     */
    public User(long ID) {
        this.u_id = ID;
    }
}
