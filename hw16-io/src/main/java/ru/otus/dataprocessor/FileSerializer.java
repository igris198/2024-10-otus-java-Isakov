package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {
    private static final Logger logger = LoggerFactory.getLogger(FileSerializer.class);

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        File file = new File(fileName);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, data);
        } catch (IOException e) {
            logger.error("Error in serialize() method: {}",e.getMessage());
        }
    }
}
