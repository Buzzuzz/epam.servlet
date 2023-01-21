package com.servlet.ejournal.services;

import com.servlet.ejournal.exceptions.ValidationError;
import com.servlet.ejournal.model.entities.Course;
import com.servlet.ejournal.model.entities.UserCourse;
import com.servlet.ejournal.services.dto.FullCourseDTO;
import com.servlet.ejournal.exceptions.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDTO> getAllCourses(int limit, int offset, String sorting, Map<String, String[]> filters);

    Optional<FullCourseDTO> getCourseDTO(Course course);

    Course getCourseFromDTO(FullCourseDTO courseDTO);

    ValidationError updateCourse(FullCourseDTO courseDTO) throws ServiceException;

    ValidationError createCourse(FullCourseDTO courseDTO) throws ServiceException;

    long deleteCourse(long id) throws ServiceException;

    ValidationError enrollStudent(long userId, long courseId);

    ValidationError withdrawStudent(long userId, long courseId);

    double getStudentMark (long courseId, long userId);
    ValidationError updateStudentMark (UserCourse userCourse, double newMark);

    Optional<Course> getCourse(long id);

    int getCourseCount(Map<String, String[]> filters);
}
