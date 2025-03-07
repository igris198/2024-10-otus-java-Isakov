package ru.otus.crm.service;

import ru.otus.crm.model.Client;

public interface DBServiceClientExt {
    Client saveClient(String name, String address, String phones);
}
