package services;

import exceptions.ErrorType;
import exceptions.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import model.entities.Course;
import services.dto.FullCourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDTO> getAllCourses();
    Optional<FullCourseDTO> getCourseDTO(Course course);
    Course getCourseFromDTO (FullCourseDTO courseDTO);
    ErrorType updateCourse(FullCourseDTO courseDTO) throws ServiceException;
    ErrorType createCourse(FullCourseDTO courseDTO) throws ServiceException;
    Optional<Course> getCourse(long id);

    int getCourseCount();
}
