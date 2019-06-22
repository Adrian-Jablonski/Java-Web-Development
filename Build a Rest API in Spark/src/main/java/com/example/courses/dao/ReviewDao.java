package com.example.courses.dao;

import com.example.courses.exc.DaoException;
import com.example.courses.model.Review;

import java.util.List;

public interface ReviewDao {
    void add(Review review) throws DaoException;

    List<Review> findAll();

    List<Review> findByCourseId(int courseId);
}
