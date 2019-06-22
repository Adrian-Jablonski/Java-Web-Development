package com.example.courses.dao;

import com.example.courses.exc.DaoException;
import com.example.courses.model.Course;

import java.util.List;

public interface CourseDao {
    void add(Course course) throws DaoException;

    List<Course> findAll();
}
