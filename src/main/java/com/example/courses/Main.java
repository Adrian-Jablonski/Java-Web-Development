package com.example.courses;

import static spark.Spark.get;

public class Main {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World"); // Find page on http://localhost:4567/hello
        get("/", (req, res) -> "Welcome");
    }
}
