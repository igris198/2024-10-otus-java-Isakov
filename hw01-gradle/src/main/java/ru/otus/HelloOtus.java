package ru.otus;

import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;

public class HelloOtus {
    public static void main(String[] args) {
        List<String> list = Arrays.asList("Hello", "OTUS", "!");
        System.out.println(Joiner.on(" ").join(list));
    }
}