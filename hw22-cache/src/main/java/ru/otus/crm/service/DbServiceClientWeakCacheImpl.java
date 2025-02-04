package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientWeakCacheImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientWeakCacheImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<String, Client> myCache;

    public DbServiceClientWeakCacheImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.myCache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                myCache.put(String.valueOf(clientId), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Optional<Client> result = Optional.ofNullable(myCache.get(String.valueOf(id)));
        if (result.isEmpty()) {
            result = transactionRunner.doInTransaction(connection -> {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                myCache.put(String.valueOf(id), clientOptional.orElse(null));
                return clientOptional;
            });
        }
        return result;
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }

    public void addListener(HwListener<String, Client> listener) {
        myCache.addListener(listener);
    }

    public void removeListener(HwListener<String, Client> listener) {
        myCache.removeListener(listener);
    }
}
