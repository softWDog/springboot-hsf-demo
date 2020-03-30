package com.chinaunicom.item;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

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

        Connection connection = dataSource.getConnection();
        ScriptRunner runner = new ScriptRunner(connection);
        runner.setAutoCommit(true);
        runner.setStopOnError(false);
        runner.setLogWriter(null);
        runner.setErrorLogWriter(null);
        runScript(runner, "CreateDB.sql");
    }

    public static void runScript(ScriptRunner runner, String resource) throws IOException, SQLException {
        Reader reader = Resources.getResourceAsReader(resource);
        runner.runScript(reader);
    }
}
