package ru.petrelevich.service;

import reactor.core.publisher.Flux;
import ru.petrelevich.domain.Message;

public interface DataStoreExt extends DataStore {
    Flux<Message> loadAllMessages();
}
