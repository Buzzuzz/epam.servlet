package services;

import jakarta.servlet.http.HttpServletRequest;
import model.entities.Course;
import services.dto.FullCourseDTO;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<FullCourseDTO> getAllCourses(HttpServletRequest req);
    Optional<FullCourseDTO> getCourseDTO(Course course);

    Optional<Course> getCourse(long id);
}
