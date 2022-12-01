package entities;

import lombok.Data;

@Data
public class Topic {
    private final long t_id;
    private String name;
    private String description;
}
