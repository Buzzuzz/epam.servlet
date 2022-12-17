package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.sql.Timestamp;

/**
 * Representation of course table in database. <br>
 * Annotated with lombok <code>@Data</code> annotation,
 * which means that class contains all trivial constructors, getters, setters (except IDs).
 */
@Data @AllArgsConstructor
public class Course {
    private final long c_id;
    private String name;
    private String description;
    private Timestamp start_date;
    private Timestamp end_date;
}
