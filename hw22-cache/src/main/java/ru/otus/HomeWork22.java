package ru.otus;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.flywaydb.core.Flyway;

import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientWeakCacheImpl;
import ru.otus.jdbc.mapper.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork22 {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork22.class);

    public static void main(String[] args) throws InterruptedException {
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);

        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient);

        HwCache<String, Client> hwCache = new MyCache<>();
        var dbServiceClient = new DbServiceClientWeakCacheImpl(transactionRunner, dataTemplateClient, hwCache);
        dbServiceClient.addListener((key, value, action) -> log.info("WeakCache action:{}, key:{}, value:{}", action, key, value));

        List<Client> testClients = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            testClients.add(dbServiceClient.saveClient(new Client(String.valueOf(i))));
        }

        var testTimeCacheStart = LocalTime.now();
        testClients.forEach(client -> log.info("Using WeakCache. Client: {}", dbServiceClient.getClient(client.getId())));
        var testTimeCacheStop = LocalTime.now();
        System.gc();
        log.info("Garbage collection started.");
        Thread.sleep(500);
        var testTimeStart = LocalTime.now();
        testClients.forEach(client -> log.info("Without WeakCache. Client: {}", dbServiceClient.getClient(client.getId())));
        var testTimeStop = LocalTime.now();

        var t1 = Duration.between(testTimeCacheStart, testTimeCacheStop);
        var t2 = Duration.between(testTimeStart, testTimeStop);
        log.info("With cache: {}", t1.toMillis());
        log.info("Without cache: {}", t2.toMillis());
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
