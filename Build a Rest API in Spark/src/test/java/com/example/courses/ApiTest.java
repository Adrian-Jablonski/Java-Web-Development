package com.example.courses;

import com.example.courses.dao.Sql2oCourseDao;
import com.example.courses.dao.Sql2oReviewDao;
import com.example.courses.exc.DaoException;
import com.example.courses.model.Course;
import com.example.courses.model.Review;
import com.example.testing.ApiClient;
import com.example.testing.ApiResponse;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ApiTest {

    public static final String PORT = "4568";
    public static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;
    private Sql2oCourseDao courseDao;
    private Sql2oReviewDao reviewDao;

    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        Api.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        courseDao = new Sql2oCourseDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        conn = sql2o.open();

        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingCoursesReturnsCreatedStatus() throws Exception {
        Map<String, String> values = new HashMap<>();
        values.put("name", "test");
        values.put("url", "http://test.com");

        ApiResponse res = client.request("POST", "/courses", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }

    @Test
    public void coursesCanBeAccessedById() throws Exception {
        Course course = newTestCourse();
        courseDao.add(course);

        ApiResponse res = client.request("GET", "/courses/" + course.getId());

        Course retrieved = gson.fromJson(res.getBody(), Course.class);

        assertEquals(course, retrieved);
    }

    @Test
    public void missingCoursesReturnNotFoundStatus() throws Exception {
        ApiResponse res = client.request("GET", "/courses/42");

        assertEquals(404, res.getStatus());
    }

    @Test
    public void addingReviewReturnsCreatedStatus() throws Exception {
        Course course = newTestCourse();
        courseDao.add(course);
        Map<String, Object> values = new HashMap<>();
        values.put("rating", 5);
        values.put("comment", "Test");

        ApiResponse res = client.request("POST", "/courses/" + course.getId() + "/reviews", gson.toJson(values));

        assertEquals(201, res.getStatus());

    }

    @Test
    public void addingReviewToUnknownCourseThrowsError() {
        Map<String, Object> values = new HashMap<>();
        values.put("rating", 5);
        values.put("comment", "Test");

        ApiResponse res = client.request("POST", "/courses/42/reviews", gson.toJson(values));

        assertEquals(500, res.getStatus());
    }

    @Test
    public void multipleReviewsReturnedForCourse() throws Exception {
        Course course = newTestCourse();
        courseDao.add(course);
        reviewDao.add(new Review(course.getId(), 5, "test comment 1"));
        reviewDao.add(new Review(course.getId(), 4, "test comment 2"));

        ApiResponse res = client.request("GET", String.format("/courses/%d/reviews", course.getId()));
        Review[] reviews = gson.fromJson(res.getBody(), Review[].class);

        assertEquals(2, reviews.length);
    }

    private Course newTestCourse() {
        return new Course("Test", "http://test.com");
    }
}