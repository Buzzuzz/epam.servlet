package com.servlet.ejournal.services.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class UserCourseDTO {
    private UserDTO user;
    private long userCourseId;
    private long userId;
    private long courseId;
    private Timestamp registrationDate;
    private double finalMark;
}
