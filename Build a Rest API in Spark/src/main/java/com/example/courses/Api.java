package com.example.courses;

import com.example.courses.dao.CourseDao;
import com.example.courses.dao.Sql2oCourseDao;
import com.example.courses.model.Course;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.post;

public class Api {
    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o("jdbc:h2:`reviews.db; INIT=RUNSCRIPT from 'classpath:db/init.sql");
        CourseDao courseDao = new Sql2oCourseDao(sql2o);

        Gson gson = new Gson();

        post("/courses", "application/json", (req, res) -> {
            Course course = gson.fromJson(req.body(), Course.class);    // Places the request body into the Course class
            courseDao.add(course);
            res.status(201);
            return course;
        }, gson::toJson);   // turns response into a JSON object

        get("/courses", "application/json",
                (req, res) -> courseDao.findAll(), gson::toJson);

        get("/courses/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            // TODO: What if this is not found?
            Course course = courseDao.findById(id);
            return null;
        });

        after((req, res) -> {
            res.type("application/json");   // Sets each response to have a tes type of application/json
        });
    }
}
