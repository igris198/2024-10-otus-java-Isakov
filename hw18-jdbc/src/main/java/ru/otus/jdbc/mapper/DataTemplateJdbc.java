package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return getResult(rs);
                }
                return null;
            } catch (SQLException e) {
                log.error("Error in findById():{}",e.getMessage());
                throw new DataTemplateException(e);
            }
        });
    }

    private T getResult(ResultSet rs) {
        try {
            T object = entityClassMetaData.getConstructor().newInstance();
            for (Field field : entityClassMetaData.getAllFields()) {
                var fieldValue = rs.getObject(field.getName());
                field.setAccessible(true);
                field.set(object, fieldValue);
            }
            return object;
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            log.error("Error in getResult():{}",e.getMessage());
            throw new DataTemplateException(e);
        }
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
            var tList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    tList.add(getResult(rs));
                }
                return tList;
            } catch (SQLException e) {
                log.error("Error in findAll():{}",e.getMessage());
                throw new DataTemplateException(e);
            }
        })
        .orElseThrow(() -> {
            log.error("Error in findAll().executeSelect");
            return new RuntimeException("Unexpected error");
        } );
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getTObjectFieldsValuesWithoutId(client));
        } catch (Exception e) {
            log.error("Error in insert():{}",e.getMessage());
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getTObjectFieldsValuesWithoutId(T client) {
        List<Object> resultList = new ArrayList<>();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                resultList.add(field.get(client));
            } catch (IllegalAccessException e) {
                log.error("Error in getTObjectFieldsValuesWithoutId():{}",e.getMessage());
                throw new DataTemplateException(e);
            }
        }
        return resultList;
    }

    private Object getTObjectIdFieldValue(T client) {
        try {
            var field = entityClassMetaData.getIdField();
            field.setAccessible(true);
            return field.get(client);
        } catch (Exception e) {
            log.error("Error in getTObjectIdFieldValue():{}",e.getMessage());
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        List<Object> paramsList = getTObjectFieldsValuesWithoutId(client);
        paramsList.add(getTObjectIdFieldValue(client));
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), paramsList);
        } catch (Exception e) {
            log.error("Error in update():{}",e.getMessage());
            throw new DataTemplateException(e);
        }
    }
}
