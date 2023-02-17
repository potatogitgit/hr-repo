package org.example;

import org.example.startup.GoogleCalendarStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
@ComponentScan
public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        SpringApplication.run(Main.class, args);
    }

}