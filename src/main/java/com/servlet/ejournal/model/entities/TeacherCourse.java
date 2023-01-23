package com.servlet.ejournal.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TeacherCourse {
    private long teacherCourseId;
    private long tch_id;
    private long c_id;
}
