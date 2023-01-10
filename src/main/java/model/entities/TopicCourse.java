package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representation of join table between topics and courses in database. <br>
 * Annotated with lombok <code>@Data</code> annotation, which means
 * that class contains all trivial constructors, getter, setters (except IDs).
 */
@Data
@AllArgsConstructor
public class TopicCourse {
    private final long t_id;
    private final long c_id;
}
