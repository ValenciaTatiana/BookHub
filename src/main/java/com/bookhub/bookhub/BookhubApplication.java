package com.bookhub.bookhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bookhub")
public class BookhubApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookhubApplication.class, args);
    }
}
