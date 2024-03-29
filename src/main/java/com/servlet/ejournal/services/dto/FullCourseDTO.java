package com.servlet.ejournal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class FullCourseDTO {
    // Course data
    private final long courseId;
    private long currentTopicId;
    private long currentTeacherId;
    private String courseName;
    private String courseDescription;
    private Timestamp startDate;
    private Timestamp endDate;
    private long duration;
    private long enrolled;

    // Topic data
    private List<TopicDTO> topics;
    private String currentTopicName;
    private String currentTopicDescription;

    // User (teacher) data
    private List<UserDTO> teachers;
    private String currentTeacherName;

}
