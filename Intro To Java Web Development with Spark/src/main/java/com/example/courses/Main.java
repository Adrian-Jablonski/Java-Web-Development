package com.example.courses;

import com.example.courses.model.CourseIdea;
import com.example.courses.model.CourseIdeaDAO;
import com.example.courses.model.NotFoundException;
import com.example.courses.model.SimpleCourseIdeaDAO;
import spark.ModelAndView;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;
import static spark.Spark.staticFiles;

import spark.Request;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

// http://sparkjava.com/

// Installed an intelij plugin for handlebars to color code files with the HBS extension

public class Main {
    private static final String FLASH_MESSAGE_KEY = "flash_message";

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
           if (req.attribute("username") == null) {
               setFlashMessage(req, "Whoops, please sign in first!");
               res.redirect("/");
               halt();
           }
        });

        get("/", (req, res) -> {
            Map<String, String> model = new HashMap<>();
            model.put("username", req.attribute("username"));
            model.put("flashMessage", captureFlashMessage(req));

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
            model.put("flashMessage", captureFlashMessage(req));
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
            boolean added = idea.addVoter(req.attribute("username"));
            if (added) {
                setFlashMessage(req, "Thanks for your vote!");
            }
            else {
                setFlashMessage(req, "You already voted");
            }
            res.redirect("/ideas");
            return null;
        });

        get("/ideas/:slug", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            CourseIdea idea = dao.findBySlug(req.params("slug"));
            model.put("idea", idea);
            return new ModelAndView(model, "idea-detail.hbs");

        }, new HandlebarsTemplateEngine());

        exception(NotFoundException.class, (exc, req, res) -> {
           res.status(404);
           HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();
           String html = engine.render(
                   new ModelAndView(null, "not-found.hbs"));
           res.body(html);
        });
    }

    private static void setFlashMessage(Request req, String message) {
        req.session().attribute(FLASH_MESSAGE_KEY, message);
    }

    private static String getFlashMessage(Request req) {
        if (req.session(false) == null) {
            return null;
        }
        if (!req.session().attributes().contains(FLASH_MESSAGE_KEY)) {
            return null;
        }
        return (String) req.session().attribute(FLASH_MESSAGE_KEY);
    }

    private static String captureFlashMessage(Request req) {
        // Used to show the message once and then disappear
        String message = getFlashMessage(req);
        if (message != null) {
            req.session().removeAttribute(FLASH_MESSAGE_KEY);
        }
        return message;
    }
}
