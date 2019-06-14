package com.example.giflib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@EnableAutoConfiguration
public class AppConfig {

    public static void main(String[] args) {
        // Creates a spring application that is deployable to a web server
        SpringApplication.run(AppConfig.class, args);   // to run server, open gradle window --> Tasks --> application --> bootRun -- Right click and click run. Application is opened on port 8080 (http://localhost:8080/)

    }
}
