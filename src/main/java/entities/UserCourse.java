package entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserCourse {
    private final long u_c_id;
    private final long u_id;
    private final long c_id;
    private Timestamp registration_date;
    private double final_mark;
}
