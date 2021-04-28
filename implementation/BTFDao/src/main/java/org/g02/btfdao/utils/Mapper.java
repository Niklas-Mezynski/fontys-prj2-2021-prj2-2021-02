package org.g02.btfdao.utils;

import org.g02.btfdao.annotations.TableName;
import org.g02.btfdao.dao.Savable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Mapper {
    //Slower?
    public static <E extends Savable> List<SQLField> getPrimaryKeyFields(Class<? extends Savable> a) {
        return getPrimaryKeyFields(getAllSQLFields(a));
    }

    public static <E extends Savable> List<SQLField> getPrimaryKeyFields(Collection<SQLField> fields){
        return fields.stream()
                .filter(SQLField::isPrimaryKey)
                .collect(Collectors.toUnmodifiableList());
    }
    public static <E extends Savable> List<Object> getPrimaryKeyFieldValues(Class<? extends Savable> a, Object entity){
        return getPrimaryKeyFieldValues(getAllSQLFields(a),entity);
    }
    public static <E extends Savable> List<Object> getPrimaryKeyFieldValues(Collection<SQLField> fields, Object entity){
        return fields.stream()
                .filter(SQLField::isPrimaryKey)
                .map(s->s.getFieldContent(entity))
                .collect(Collectors.toUnmodifiableList());
    }
    public static List<SQLField> getAllSQLFields(Class<? extends Savable> entityType){
        return Arrays.stream(entityType.getDeclaredFields())
                .map(SQLField::new)
                .filter(SQLField::isNotIgnored)
                .collect(Collectors.toUnmodifiableList());
    }
    public static String getSQLTableName(Class<?> entityType){
        //assert Savable.class.isAssignableFrom(entityType):"Invalid type of field specified, must be primitive or Savable";
        if (entityType.isAnnotationPresent(TableName.class))
            return entityType.getAnnotation(TableName.class).value();
        else throw new IllegalStateException("Tablename requested, but required annotation is not present for: "+entityType.getName());
    }
}
