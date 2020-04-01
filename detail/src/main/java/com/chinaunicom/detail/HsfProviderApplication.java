package com.chinaunicom.detail;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

@SpringBootApplication
@ComponentScan(basePackages = {"com.chinaunicom.detail.controller", "com.chinaunicom.hsf.support","com.chinaunicom.detail.aspect"})
public class HsfProviderApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(HsfProviderApplication.class, args);
    }

    /**
     * @param webApplicationContext
     * @Description: 过期主要是因为，ali-tomcat这些版本较低
     */
    @Bean
    public Log4jConfigListener log4jConfigListener(WebApplicationContext webApplicationContext){
        ServletContext servletContext = webApplicationContext.getServletContext();
        servletContext.setInitParameter("log4jConfigLocation","classpath:log4j.properties");
        Log4jConfigListener log4jConfigListener = new Log4jConfigListener();
        log4jConfigListener.contextInitialized(new ServletContextEvent(servletContext));
        return log4jConfigListener;
    }

}
