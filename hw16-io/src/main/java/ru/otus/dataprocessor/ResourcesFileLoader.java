package ru.otus.dataprocessor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesFileLoader.class);

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (InputStream inputStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(inputStream, new TypeReference<List<Measurement>>() {});
        } catch (IOException e) {
            logger.error("Error in load() method: {}",e.getMessage());
            throw new FileProcessException(e);
        }
    }
}
