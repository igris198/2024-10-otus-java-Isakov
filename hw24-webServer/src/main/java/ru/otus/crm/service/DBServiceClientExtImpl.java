package ru.otus.crm.service;

import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.Arrays;
import java.util.List;

public class DBServiceClientExtImpl implements DBServiceClientExt{
    DBServiceClient dbServiceClient;

    public DBServiceClientExtImpl(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    public Client saveClient(String name, String street, String phones) {
        Address address = new Address(null, street);
        List<Phone> phonesList = Arrays.stream(phones.split(",")).map(phone -> new Phone(null, phone.trim())).toList();
        Client client = new Client(null, name, address, phonesList);
        return dbServiceClient.saveClient(client);
    }
}
