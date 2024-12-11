package ru.otus;

import ru.otus.proxy.ProxyTestLogging;
import ru.otus.proxy.TestLoggingImpl;
import ru.otus.proxy.TestLoggingInterface;

import java.lang.reflect.InvocationTargetException;

public class Hw10Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        TestLoggingInterface myClass = ProxyTestLogging.createMyClass(new TestLoggingImpl());

        myClass.calculation(1);
        myClass.calculation(2, 3);
        myClass.calculation(4, 5, "6");
    }
}