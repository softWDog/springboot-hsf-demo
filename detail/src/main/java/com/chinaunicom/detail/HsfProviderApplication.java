package com.chinaunicom.detail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chinaunicom.detail.controller", "com.chinaunicom.hsf.support"})
public class HsfProviderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HsfProviderApplication.class, args);
    }

}
