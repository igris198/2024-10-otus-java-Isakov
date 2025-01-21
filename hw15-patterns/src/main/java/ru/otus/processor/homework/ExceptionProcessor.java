package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.Processor;

public class ExceptionProcessor implements Processor {
    DateTimeProvider dateTimeProvider;

    public ExceptionProcessor(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond()%2 == 0) {
            throw new ProcessorException("Exception to do 3!");
        }
        return message;
    }

    public static class ProcessorException extends RuntimeException{
        public ProcessorException(String message) {
            super(message);
        }
    }
}
