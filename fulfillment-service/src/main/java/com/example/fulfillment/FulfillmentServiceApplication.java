package com.example.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.fulfillment")
public class FulfillmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FulfillmentServiceApplication.class, args);
    }
}