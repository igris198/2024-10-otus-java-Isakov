package ru.otus.testlauncher;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Создать "запускалку теста". На вход она должна получать имя класса с тестами, в котором следует найти и запустить
 * методы отмеченные аннотациями и пункта 1.
 * Алгоритм запуска должен быть следующий::
 * метод(ы) Before
 * текущий метод Test
 * метод(ы) After
 * для каждой такой "тройки" надо создать СВОЙ экземпляр класса-теста.
 * Исключение в одном тесте не должно прерывать весь процесс тестирования.
 * На основании возникших во время тестирования исключений вывести статистику выполнения тестов (сколько прошло успешно, сколько упало, сколько было всего)
 * "Запускалка теста" не должна иметь состояние, но при этом весь функционал должен быть разбит на приватные методы.
 * Надо придумать, как передавать информацию между методами.
 */
public class TestLauncher {
    private static final Logger logger = LoggerFactory.getLogger(TestLauncher.class);

    public static void run(Class<?> testClass) {
        if (testClass == null) {
            testClass = getTestSetClass();
        }

        List<Method> beforeMethods = getAnnotatedMethods(testClass, Before.class);
        List<Method> testMethods = getAnnotatedMethods(testClass, Test.class);
        List<Method> afterMethods = getAnnotatedMethods(testClass, After.class);

        executeTests(testClass, beforeMethods, testMethods, afterMethods);
    }

    private static Class<?> getTestSetClass() {
        final Class<?> testClass;

        try (InputStream inputStream = TestLauncher.class.getClassLoader().getResourceAsStream("testlauncher.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            testClass = Class.forName(properties.getProperty("test.set").trim());
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Ошибка чтения набора тестов:", e);
            throw new RuntimeException(e);
        }
        return testClass;
    }

    private static List<Method> getAnnotatedMethods(Class<?> testClass, Class<? extends Annotation> annotationClass) {
        final List<Method> methodsList = new ArrayList<>();
        for (Method method : testClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotationClass)) {
                methodsList.add(method);
            }
        }
        return methodsList;
    }

    private static void executeTests(Class<?> testClass, List<Method> beforeMethods, List<Method> testMethods, List<Method> afterMethods) {
        int testsPassed = 0;
        final Constructor<?> testSetConstructor = getConstructor(testClass);

        for (Method testMethod : testMethods) {
            Object testClassObject = getTestClassObject(testSetConstructor);
            if (invokeBeforeMethods(testClassObject, beforeMethods)) {
                if (invokeTestMethod(testClassObject, testMethod)) {
                    testsPassed++;
                }
            }
            invokeAfterMethods(testClassObject, afterMethods);
        }
        logTestResults(testMethods, testsPassed);
    }

    private static void logTestResults(List<Method> testMethods, int testsPassed) {
        logger.atInfo()
                .setMessage("Общее количество тестов: {}. Пройдено успешно: {}. Провалено: {}")
                .addArgument(testMethods.size())
                .addArgument(testsPassed)
                .addArgument(testMethods.size() - testsPassed)
                .log();
    }

    private static Object getTestClassObject(Constructor<?> testSetConstructor) {
        final Object testClassObject;
        try {
            testClassObject = testSetConstructor.newInstance();
            return testClassObject;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            logger.error("Ошибка создания объекта класса-теста:", e);
            throw new RuntimeException(e);
        }
    }

    private static Constructor<?> getConstructor(Class<?> testClass) {
        final Constructor<?> testSetConstructor;
        try {
            testSetConstructor = testClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            logger.error("Ошибка получения конструктора класса-теста:", e);
            throw new RuntimeException(e);
        }
        return testSetConstructor;
    }

    private static boolean invokeBeforeMethods(Object testClassObject, List<Method> methods) {
        try {
            for (Method beforeMethod : methods) {
                beforeMethod.invoke(testClassObject);
            }
            return true;
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Ошибка в invokeBeforeMethods:", e);
            return false;
        }
    }

    private static boolean invokeTestMethod(Object testClassObject, Method testMethod) {
        try {
            testMethod.invoke(testClassObject);
            logger.info("Тест '{}' пройден успешно",testMethod.getName());
            return true;
        } catch (Exception e) {
            logger.error("Тест '{}' провален :", testMethod.getName(), e);
            return false;
        }
    }

    private static void invokeAfterMethods(Object testClassObject, List<Method> methods) {
        try {
            for (Method afterMethod : methods) {
                afterMethod.invoke(testClassObject);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Ошибка в invokeAfterMethods:", e);
        }
    }

}