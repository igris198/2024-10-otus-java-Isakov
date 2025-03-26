package ru.otus.protobuf;

import java.util.concurrent.TimeUnit;

public class Util {
    public static void sleepNumSec(int sec) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(sec));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
