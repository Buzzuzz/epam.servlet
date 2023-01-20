package com.my.project.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Representation of topic table in database. <br>
 * Annotated with lombok <code>@Data</code> annotation, which means
 * that class contains all trivial constructors, getters, setters.
 */
@Data @AllArgsConstructor
public class Topic {
    private long t_id;
    private String name;
    private String description;
}
