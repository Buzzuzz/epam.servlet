package model.dao.impl;

import model.dao.DAO;
import model.entities.TopicCourse;

import java.sql.Connection;
import java.util.Collection;
import java.util.Optional;

public class TopicCourseDAO implements DAO<TopicCourse> {
    @Override
    public Optional<TopicCourse> get(Connection con, long id) {
        return Optional.empty();
    }

    @Override
    public Collection<TopicCourse> getAll(Connection con) {
        return null;
    }

    @Override
    public long update(Connection con, TopicCourse entity) {
        return 0;
    }

    @Override
    public long delete(Connection con, long id) {
        return 0;
    }

    @Override
    public long save(Connection con, TopicCourse entity) {
        return 0;
    }
}
