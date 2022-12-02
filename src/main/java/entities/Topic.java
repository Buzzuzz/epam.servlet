package entities;

import lombok.Data;

/**
 * Representation of topic table in database. <br>
 * Annotated with lombok <code>@Data</code> annotation, which means
 * that class contains all trivial constructors, getters, setters (except IDs).
 */
@Data
public class Topic {
    private final long t_id;
    private String name;
    private String description;
}
