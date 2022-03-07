package com.example.springtransactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
public class SpringTransactionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTransactionsApplication.class, args);
    }

    @Bean
    void init() {
        System.out.println("Initialization.");
        createNewTable("C:\\TMP\\sqlite_db","test.db");
    }

    public static void createNewTable(String directoryName, String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + directoryName + "\\" + fileName;

        // SQL statement for creating a new table
        String dropTable = "drop table if exists BOOKINGS ;";
//        String createTable = "create table BOOKINGS(ID serial, FIRST_NAME varchar(5) NOT NULL);";
        String createTable = "CREATE TABLE IF NOT EXISTS BOOKINGS(ID serial, FIRST_NAME TEXT NOT NULL CHECK(typeof(FIRST_NAME) = 'text' AND length(FIRST_NAME) <= 5));";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(dropTable);
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {
        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("driverClassName"));
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("user"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

}
