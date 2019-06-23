package com.example.courses.dao;

import com.example.courses.exc.DaoException;
import com.example.courses.model.Course;
import com.example.courses.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.*;

public class Sql2oReviewDaoTest {

    private Sql2oCourseDao courseDao;
    private Sql2oReviewDao reviewDao;
    private Connection conn;
    private Course course;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";    // Tells program to run init.sql script on startup
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        courseDao = new Sql2oCourseDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);

        conn = sql2o.open();

        course = newTestCourse();
        courseDao.add(course);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingReviewSetsId() throws Exception {
        int courseId = course.getId();  // Need to create course and get course id first so test does not fail because of foreign key

        Review review = newTestReview(courseId);
        int originalReviewId = review.getId();

        reviewDao.add(review);

        assertNotEquals(originalReviewId, review.getId());
    }

    @Test
    public void multipleReviewsAreFoundWhenTheyExistForACourse() throws Exception {
        reviewDao.add(newTestReview(course.getId()));
        reviewDao.add(new Review(course.getId(), 4, "Test 2"));

        List<Review> reviews = reviewDao.findByCourseId(course.getId());

        assertEquals(2, reviews.size());
    }

    @Test
    public void noReviewReturnsEmptyList() throws Exception {
        assertEquals(0, reviewDao.findAll().size());
    }

    @Test (expected = DaoException.class)
    public void addingAReviewToANoneExistingCourseFails() throws Exception {
        Review review = new Review(42, 5, "Test");

        reviewDao.add(review);
    }

    private Review newTestReview(int courseId) {
        return new Review(courseId, 4, "Test Review");
    }

    private Course newTestCourse() {
        return new Course("Test", "http://test.com");
    }
}