package ru.otus;

import org.hibernate.cfg.Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;

/*
    // Стартовая страница
    http://localhost:8080

    // Страница добавления клиентов
    http://localhost:8080/clients
    // Страница списка клиентов

    http://localhost:8080/allClients
*/

@SpringBootApplication
public class WebServerHW28 {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        SpringApplication.run(WebServerHW28.class, args);
    }
}
