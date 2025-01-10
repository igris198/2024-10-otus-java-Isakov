package ru.otus.listener.homework;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

public class HistoryListener implements Listener, HistoryReader {
    private static final Logger logger = LoggerFactory.getLogger(HistoryListener.class);

    private final Map<Long, Message> historyMap = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        logger.info("Message saved with id: {}", msg.getId());

        var historyField13 = new ObjectForMessage();
        var field13Data = new ArrayList<>(msg.getField13().getData());
        historyField13.setData(field13Data);

        historyMap.put(msg.getId(), msg.toBuilder().field13(historyField13).build());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var msg = historyMap.get(id);
        logger.info("Message restored with id: {}", msg.getId());
        return Optional.of(msg);
    }
}
