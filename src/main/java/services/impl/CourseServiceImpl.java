package services.impl;

import jakarta.servlet.http.HttpServletRequest;
import model.dao.DAO;
import model.dao.DataSource;
import model.dao.impl.CourseDAO;
import model.entities.Course;
import services.CourseService;

import java.sql.Connection;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private static final DAO<Course> dao = CourseDAO.getInstance();

    // Suppress constructor
    private CourseServiceImpl() {
    }

    public static CourseService getInstance() {
        return Holder.service;
    }

    private static class Holder {
        private static final CourseService service = new CourseServiceImpl();
    }
    @Override
    public List<Course> getAllCourses(HttpServletRequest req) {
        Connection con = null;
        try {
            con = DataSource.getConnection();
            return (List<Course>) dao.getAll(con);
        } finally {
            DataSource.close(con);
        }
    }
}
