package ru.otus.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Log;

public class TestLoggingImpl implements TestLoggingInterface{
    private static final Logger logger = LoggerFactory.getLogger(TestLoggingImpl.class);

    @Override
    public void calculation(int param1) {
        logger.info("method: calculation, param: {}", param1);
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        logger.info("method: calculation, params: {}, {} ", param1, param2);
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        logger.info("method: calculation, params: {}, {}, {}", param1, param2, param3);
    }
}
