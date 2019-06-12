package com.example.courses;

import spark.ModelAndView;

import static spark.Spark.get;
import static spark.Spark.post;

import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

// http://sparkjava.com/

// Installed an intelij plugin for handlebars to color code files with the HBS extension

public class Main {
    public static void main(String[] args) {
        //get("/hello", (req, res) -> "Hello World"); // Find page on http://localhost:4567/hello

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
    }
}
