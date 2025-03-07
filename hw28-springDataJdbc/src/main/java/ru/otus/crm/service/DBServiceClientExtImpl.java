package ru.otus.crm.service;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.sessionmanager.TransactionManager;

@Service
public class DBServiceClientExtImpl implements DBServiceClientExt {
    private static final Logger log = LoggerFactory.getLogger(DBServiceClientExtImpl.class);

    private final TransactionManager transactionManager;
    private final DBServiceClient dbServiceClient;
    private final DBServiceAddress dbServiceAddress;
    private final DBServicePhone dbServicePhone;

    public DBServiceClientExtImpl(
            TransactionManager transactionManager,
            DBServiceClient dbServiceClient,
            DBServiceAddress dbServiceAddress,
            DBServicePhone dbServicePhone) {
        this.transactionManager = transactionManager;
        this.dbServiceClient = dbServiceClient;
        this.dbServiceAddress = dbServiceAddress;
        this.dbServicePhone = dbServicePhone;
    }

    @Override
    public Client saveClient(String name, String street, String phones) {
        return transactionManager.doInTransaction(() -> {
            log.info("DBServiceClientExtImpl start saveClient");
            Address address = dbServiceAddress.saveAddress(new Address(null, street));
            Client client = dbServiceClient.saveClient(new Client(null, name, address.getId(), null));
            List<Phone> phonesList = Arrays.stream(phones.split(","))
                    .map(phone -> new Phone(null, phone.trim(), client.getId()))
                    .toList();
            phonesList.forEach(dbServicePhone::savePhone);

            log.info("DBServiceClientExtImpl result saveClient: {}", client);

            return client;
        });
    }
}
