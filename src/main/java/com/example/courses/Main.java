package com.example.courses;

import spark.ModelAndView;

import static spark.Spark.get;
import spark.template.handlebars.HandlebarsTemplateEngine;

// http://sparkjava.com/

// Installed an intelij plugin for handlebars to color code files with the HBS extension

public class Main {
    public static void main(String[] args) {
        //get("/hello", (req, res) -> "Hello World"); // Find page on http://localhost:4567/hello

        get("/", (req, res) -> {
            return new ModelAndView(null, "index.hbs");
        }, new HandlebarsTemplateEngine());     // Renders a handlebar index page
    }
}
