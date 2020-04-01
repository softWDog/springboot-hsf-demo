package com.chinaunicom.item;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootApplication
@MapperScan(basePackages = "com.chinaunicom.item.mapper")
@ComponentScan(basePackages = {"com.chinaunicom.hsf.support", "com.chinaunicom.item.impl"})
public class ItemApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        try (Connection connection = dataSource.getConnection();){
            ScriptRunner runner = new ScriptRunner(connection);
            runner.setAutoCommit(true);
            runner.setStopOnError(false);
            runner.setLogWriter(null);
            runner.setErrorLogWriter(null);
            runScript(runner, "CreateDB.sql");
        }
    }

    @Bean
    public Log4jConfigListener log4jConfigListener(WebApplicationContext webApplicationContext){
        Log4jConfigListener log4jConfigListener = new Log4jConfigListener();
        ServletContext servletContext = webApplicationContext.getServletContext();
        servletContext.setInitParameter("log4jConfigLocation","classpath:log4j.properties");
        log4jConfigListener.contextInitialized(new ServletContextEvent(servletContext));
        return log4jConfigListener;
    }

    public static void runScript(ScriptRunner runner, String resource) throws IOException, SQLException {
        try(Reader reader = Resources.getResourceAsReader(resource);){
            runner.runScript(reader);
        }
    }
}
