package model.entities;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Representation of join table in database between users and courses. <br>
 * Annotated with lombok <code>@Data</code> annotation, which means that
 * class contains all trivial constructors, getters, setters (except IDs).
 */
@Data
public class UserCourse {
    private final long u_c_id;
    private final long u_id;
    private final long c_id;
    private Timestamp registration_date;
    private double final_mark;
}
