package ru.otus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.DateTimeProvider;
import ru.otus.processor.homework.ExceptionProcessor;
import ru.otus.processor.homework.ProcessorChangeFields;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HomeWorkTest {

    @Test
    @DisplayName("Тестируем что ExceptionProcessor будет выбрасывать исключение в четную секунду")
    void ExceptionProcessorTest(){
        var message = new Message.Builder(1L).field7("field7").build();

        DateTimeProvider dateTimeProvider = mock(DateTimeProvider.class);
        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2025, 01, 03, 11, 11, 10));

        ExceptionProcessor exceptionProcessor = new ExceptionProcessor(dateTimeProvider);

        Exception exception = assertThrows(ExceptionProcessor.ProcessorException.class,() -> exceptionProcessor.process(message));
        assertEquals("Exception to do 3!",exception.getMessage());

        when(dateTimeProvider.getDate()).thenReturn(LocalDateTime.of(2025, 01, 03, 11, 11, 11));
        assertThat(exceptionProcessor.process(message)).isEqualTo(message);
    }

    @Test
    @DisplayName("Тестируем что ProcessorChangeFields поменяет местами значения field11 и field12")
    void ProcessorChangeFieldsTest(){
        var message = new Message.Builder(1L).field11("field11").field12("field12").build();

        var processorChangeFields = new ProcessorChangeFields();

        var result = processorChangeFields.process(message);

        assertThat(message.getField11()).isEqualTo(result.getField12());
        assertThat(message.getField12()).isEqualTo(result.getField11());
    }
}
