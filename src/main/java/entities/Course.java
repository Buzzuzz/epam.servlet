package entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Course {
    private final long c_id;
    private String name;
    private String description;
    private Timestamp start_date;
    private Timestamp end_date;
}
