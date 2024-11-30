package ru.otus.tests;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

public class TestSet {
    private static final Logger logger = LoggerFactory.getLogger(TestSet.class);

    @Test
    public void successfulTest1() {
        logger.info("@Test: successfulTest1");
    }

    @Test
    public void failedTest1() {
        logger.info("@Test: failedTest1");
        throw new AssertionError("Ошибка в методе failedTest1");
    }

    @Before
    public void dataPreparation1() {
        logger.info("@Before");
        logger.info("Подготовка данных: dataPreparation1");
    }

    @Before
    public void dataPreparation2() {
        logger.info("@Before");
        logger.info("Подготовка данных: dataPreparation2");
    }

    @After
    public void closingEvents() {
        logger.info("@After");
        logger.info("Завершающий метод: closingEvents");
    }
}
