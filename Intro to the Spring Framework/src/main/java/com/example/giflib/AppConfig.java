package com.example.giflib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan  // Instructs framework to automatically scan current package for controllers
public class AppConfig {
    public static void main(String[] args) {
        // Creates a spring application that is deployable to a web server using Tomcat
        SpringApplication.run(AppConfig.class, args);   // to run server, open gradle window --> Tasks --> application --> bootRun -- Right click and click run. Application is opened on port 8080 (http://localhost:8080/)

    }
}
