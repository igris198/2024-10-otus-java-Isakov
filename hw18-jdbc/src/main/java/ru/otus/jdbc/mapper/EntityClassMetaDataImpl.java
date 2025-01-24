package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final Class<T> clazz;
    private String classSimpleName;
    private Constructor<T> constructor;
    private Field idField;
    private List<Field> allFieldsList;
    private List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return classSimpleName == null ? classSimpleName = clazz.getSimpleName() : classSimpleName;
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return constructor == null ? constructor = clazz.getConstructor() : constructor;
        } catch (NoSuchMethodException e) {
            log.error("Error in getConstructor():{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        if (idField == null) {
            idField = Arrays.stream(clazz.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Id.class))
                    .findFirst()
                    .orElse(null);
        }
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFieldsList == null ? allFieldsList = List.of(clazz.getDeclaredFields()) : allFieldsList;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (fieldsWithoutId == null) {
            fieldsWithoutId = getAllFields().stream()
                    .filter(field -> !field.equals(getIdField()))
                    .toList();
        }
        return fieldsWithoutId;
    }
}
