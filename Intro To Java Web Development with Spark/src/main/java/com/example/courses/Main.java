package com.example.courses;

import com.example.courses.model.CourseIdea;
import com.example.courses.model.CourseIdeaDAO;
import com.example.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.Spark.staticFiles;

import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

// http://sparkjava.com/

// Installed an intelij plugin for handlebars to color code files with the HBS extension

public class Main {
    public static void main(String[] args) {
        //get("/hello", (req, res) -> "Hello World"); // Find page on http://localhost:4567/hello

        staticFileLocation("/public");  // tells server where to look for static files such as CSS files

        CourseIdeaDAO dao = new SimpleCourseIdeaDAO();

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.cookie("username"));

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());     // Renders a handlebar index page

        post("/sign-in", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");

            res.cookie("username", username); // Sets cookie so that username will be available on every page throughout the website

            model.put("username", username); // Gets the username typed into the form in the index page and assigns it a key of username which will be used to reference it in the sign-in.hbs file

            return new ModelAndView(model, "sign-in.hbs");
        }, new HandlebarsTemplateEngine());

        get("/ideas", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) -> {
            String title = req.queryParams("title");
            //TODO: This username is tied to the cookie implementation
            CourseIdea courseIdea = new CourseIdea(title, req.cookie("username"));
            dao.add(courseIdea);

            res.redirect(("/ideas"));
            return null;
        });
    }
}
