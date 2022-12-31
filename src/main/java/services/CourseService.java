package services;

import jakarta.servlet.http.HttpServletRequest;
import model.entities.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAllCourses(HttpServletRequest req);
}
