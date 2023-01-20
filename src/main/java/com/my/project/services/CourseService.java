package com.my.project.services;

import com.my.project.exceptions.ValidationError;
import com.my.project.model.entities.Course;
import com.my.project.model.entities.UserCourse;
import com.my.project.services.dto.FullCourseDTO;
import com.my.project.exceptions.ServiceException;

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
