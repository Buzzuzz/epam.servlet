package services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class FullCourseDTO {
    // Course data
    private final long courseId;
    private String courseName;
    private String courseDescription;
    private Timestamp startDate;
    private Timestamp endDate;

    // Topic data
    private String topicName;
    private String topicDescription;

    // User (teacher) data
    private String firstName;
    private String lastName;

}
