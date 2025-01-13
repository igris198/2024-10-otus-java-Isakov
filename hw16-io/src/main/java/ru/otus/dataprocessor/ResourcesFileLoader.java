package ru.otus.dataprocessor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.Collections;
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
        StringBuilder stringForParsing = new StringBuilder("{ \"values\":");
        try (InputStream inputStream = ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while( (line = bufferedReader.readLine()) !=null) {
                stringForParsing.append(line);
            }
            stringForParsing.append("}");
            ObjectMapper objectMapper = new ObjectMapper();
            InputDataClass inputDataClass = objectMapper.readValue(stringForParsing.toString(), InputDataClass.class);
            return inputDataClass.values;
        } catch (IOException e) {
            logger.error("Error in load() method: {}",e.getMessage());
            throw new FileProcessException(e);
        }
    }

    private static class InputDataClass{
        @JsonProperty("values")
        List<Measurement> values;
    }
}
