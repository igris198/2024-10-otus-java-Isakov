package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private static final Logger log = LoggerFactory.getLogger(EntityClassMetaDataImpl.class);

    private final Class<T> clazz;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            log.error("Error in getConstructor():{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        Optional<Field> fieldOptional = Arrays.stream(clazz.getDeclaredFields()).filter(field -> field.isAnnotationPresent(Id.class)).findFirst();
        return fieldOptional.orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        var listAllFields = clazz.getDeclaredFields();
        return List.of(listAllFields);
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        var idField = getIdField();
        return getAllFields().stream().filter(field -> !field.equals(idField)).toList();
    }
}
