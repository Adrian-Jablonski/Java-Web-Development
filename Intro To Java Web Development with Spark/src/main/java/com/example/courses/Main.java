package com.example.courses;

import com.example.courses.model.CourseIdea;
import com.example.courses.model.CourseIdeaDAO;
import com.example.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
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

        // Before filters and request attributes
        before((req, res) -> {  // Not providing a path means this will run on all pages
            if (req.cookie("username") != null) {
                req.attribute("username", req.cookie("username"));
            }
        });

        before("/ideas", (req, res) -> {
            //TODO: send message about redirect
           if (req.attribute("username") == null) {
               res.redirect("/");
               halt();
           }
        });

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.attribute("username"));

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());     // Renders a handlebar index page

        post("/sign-in", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            String username = req.queryParams("username");

            res.cookie("username", username); // Sets cookie so that username will be available on every page throughout the website

            model.put("username", username); // Gets the username typed into the form in the index page and assigns it a key of username which will be used to reference it in the sign-in.hbs file

            res.redirect(("/"));
            return null;
        });

        get("/ideas", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ideas", dao.findAll());
            return new ModelAndView(model, "ideas.hbs");
        }, new HandlebarsTemplateEngine());

        post("/ideas", (req, res) -> {
            String title = req.queryParams("title");
            CourseIdea courseIdea = new CourseIdea(title, req.attribute("username"));
            dao.add(courseIdea);

            res.redirect(("/ideas"));
            return null;
        });

        post("/ideas/:slug/vote", (req, res) -> {
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            idea.addVoter(req.attribute("username"));
            res.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            model.put("voters", idea.getVoters());
            return new ModelAndView(model, "idea-detail.hbs");

        }, new HandlebarsTemplateEngine());
    }
}
