package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final EntityClassMetaData<T> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        return getSelectAllSql() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        StringBuilder fieldsTemplate = new StringBuilder();
        StringBuilder paramsTemplate = new StringBuilder();
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            fieldsTemplate.append(field.getName()).append(",");
            paramsTemplate.append("?,");
        }
        fieldsTemplate.deleteCharAt(fieldsTemplate.lastIndexOf(","));
        paramsTemplate.deleteCharAt(paramsTemplate.lastIndexOf(","));
        return "insert into " + entityClassMetaData.getName() + " (" + fieldsTemplate + ") values (" + paramsTemplate + ")";
    }

    @Override
    public String getUpdateSql() {
        StringBuilder updTemplate = new StringBuilder("update ");
        updTemplate.append(entityClassMetaData.getName()).append(" set ");
        for (Field field : entityClassMetaData.getFieldsWithoutId()) {
            updTemplate.append(field.getName()).append(" = ?,");
        }
        updTemplate.deleteCharAt(updTemplate.lastIndexOf(","));
        updTemplate.append(" where ").append(entityClassMetaData.getIdField().getName()).append(" = ?");
        return updTemplate.toString();
    }
}
