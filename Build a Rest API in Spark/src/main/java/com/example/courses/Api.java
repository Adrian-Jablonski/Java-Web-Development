package com.example.courses;

import com.example.courses.dao.CourseDao;
import com.example.courses.dao.Sql2oCourseDao;
import com.example.courses.exc.ApiError;
import com.example.courses.model.Course;
import com.google.gson.Gson;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

public class Api {
    public static void main(String[] args) {
        String datasource = "jdbc:h2:~/reviews.db";
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("java api <port> <datasource>");
                System.exit(0);
            }
            port(Integer.parseInt(args[0])); // sets port for server to run on
            datasource = args[1];
        }

        Sql2o sql2o = new Sql2o(
                String.format("%s;INIT=RUNSCRIPT from 'classpath:db/init.sql'", datasource), "", "");
        CourseDao courseDao = new Sql2oCourseDao(sql2o);

        Gson gson = new Gson();

        post("/courses", "application/json", (req, res) -> {
            Course course = gson.fromJson(req.body(), Course.class);
            courseDao.add(course);
            res.status(201);
//            res.type("application/json"); we made new after method to replace this
            return course;
        }, gson::toJson);  // turns response into a JSON object

        get("/courses", "application/json",
                (req, res) -> courseDao.findAll(), gson::toJson);

        get("/courses/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            // TODO: What if this is not found?
            Course course = courseDao.findById(id);
            if (course == null) {
                throw new ApiError(404, "Could not find course with ID " + id );
            }
            return course;
        }, gson::toJson);

        exception(ApiError.class, (exc, req, res) -> {
            ApiError err = (ApiError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        after((req, res) -> {
            res.type("application/json");   // Sets each response to have a tes type of application/json
        });
    }
}
