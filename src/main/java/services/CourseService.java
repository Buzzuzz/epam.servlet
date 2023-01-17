package services;

import exceptions.ErrorType;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.Course;
import services.dto.FullCourseDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDTO> getAllCourses(int limit, int[] pages, int currentPage, int offset, String sorting, Map<String, String[]> filters);

    Optional<FullCourseDTO> getCourseDTO(Course course);

    Course getCourseFromDTO(FullCourseDTO courseDTO);

    ErrorType updateCourse(FullCourseDTO courseDTO) throws ServiceException;

    ErrorType createCourse(FullCourseDTO courseDTO) throws ServiceException;

    long deleteCourse(long id) throws ServiceException;

    ErrorType enrollUser(long userId, long courseId);

    Optional<Course> getCourse(long id);

    int getCourseCount(Map<String, String[]> filters);
}
