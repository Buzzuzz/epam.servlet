package services;

import exceptions.ErrorType;
import exceptions.ServiceException;
import model.entities.Course;
import model.entities.UserCourse;
import services.dto.FullCourseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDTO> getAllCourses(int limit, int offset, String sorting, Map<String, String[]> filters);

    Optional<FullCourseDTO> getCourseDTO(Course course);

    Course getCourseFromDTO(FullCourseDTO courseDTO);

    ErrorType updateCourse(FullCourseDTO courseDTO) throws ServiceException;

    ErrorType createCourse(FullCourseDTO courseDTO) throws ServiceException;

    long deleteCourse(long id) throws ServiceException;

    ErrorType enrollStudent(long userId, long courseId);

    ErrorType withdrawStudent(long userId, long courseId);

    double getStudentMark (long courseId, long userId);
    ErrorType updateStudentMark (UserCourse userCourse, double newMark);

    Optional<Course> getCourse(long id);

    int getCourseCount(Map<String, String[]> filters);
}
