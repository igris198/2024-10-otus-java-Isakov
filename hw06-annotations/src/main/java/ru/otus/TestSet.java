package ru.otus;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestSet {
    @Test
    void successfulTest1() {
        System.out.printf("1 + 1 = %d\n", 1 + 1);
    }

    @Test
    void failedTest1() {
        System.out.printf("1 / 0 = %d\n", 1 / 0);
    }

    @Before
    void dataPreparation1() {
        System.out.println("Подготовка данных: dataPreparation1");
    }

    @Before
    void dataPreparation2() {
        System.out.println("Подготовка данных: dataPreparation2");
    }

    @After
    void closingEvents() {
        System.out.println("Завершающий метод: closingEvents");
    }
}
