package com.example.courses.dao;

import com.example.courses.exc.DaoException;
import com.example.courses.model.Course;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.List;

public class Sql2oCourseDao implements CourseDao {

    private final Sql2o sql2o;

    public Sql2oCourseDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    @Override
    public void add(Course course) throws DaoException {
        String sql = "INSERT INTO courses (name, url) VALUES (:name, :url)";    // :name and :url will be replaced with getName and getUrl when .bind is called on the course
        try (Connection con = sql2o.open()) {   // Opens SQL connection
            int id = (int) con.createQuery(sql)
                    .bind(course)
                    .executeUpdate()
                    .getKey();
            course.setId(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex, "Problem adding course");
        }
    }

    @Override
    public List<Course> findAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM courses")
                    .executeAndFetch(Course.class);
        }
    }
}
