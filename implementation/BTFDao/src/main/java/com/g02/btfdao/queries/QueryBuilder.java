package com.g02.btfdao.queries;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;

public class QueryBuilder {

    private static String placeholders(int a) {
        return IntStream.range(0, a)
                .mapToObj(b -> "?")
                .collect(Collectors.joining(","));
    }

    public String createDropSQL(String[] classes) throws ClassNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        for (String aClass : classes) {
            var tableName = Mapper.getTableName(aClass);
            var format = format("DROP TABLE %1$s CASCADE;", tableName);
            stringBuilder.append(format);
        }
        return stringBuilder.toString();
    }

    public String createDatabaseSQL(String[] classes) throws ClassNotFoundException, SQLFeatureNotSupportedException, NoSuchFieldException {
        StringBuilder stringBuilder = new StringBuilder();
        Stream<Class<? extends Savable>> classStream = Arrays.stream(classes).map(s -> {
            try {
                return (Class<? extends Savable>) Class.forName(s);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });
        List<Class<? extends Savable>> collect = classStream.collect(Collectors.toList());
        for (Class<? extends Savable> aClass : collect) {
            var tableSQL = createTableSQL(aClass);
            stringBuilder.append(tableSQL);
            stringBuilder.append("\n");
        }
        for (Class<? extends Savable> aClass : collect) {
            stringBuilder.append(alterTableAddForeignKeys(aClass));
            stringBuilder.append("\n");
        }
        for (Class<? extends Savable> aClass : collect) {
            var fields = Mapper.getFields(aClass, ForeignKey.class);
            for (Field field : fields) {
                if (field.getType().isArray() && field.isAnnotationPresent(ForeignKey.class)) {
                    var annotation = field.getAnnotation(ForeignKey.class);
                    var split = annotation.value().split("#");
                    assert split.length == 2 : "ForeignKey reference has the wrong format";
                    var referenceClass = Class.forName(split[0]);
                    var referenceField = referenceClass.getField(split[1]);
                    var tableNameThis = Mapper.getTableName(aClass);
                    var columnNameThis = tableNameThis + "_" + field.getName();
                    var tableNameReference = Mapper.getTableName(referenceClass);
                    var columnNameReference = tableNameReference + "_" + referenceField.getName();
                    var tableName = columnNameThis + "_" + columnNameReference;
                    var tableSQLLine = createTableSQLLine(field, true, columnNameThis);
                    var tableSQLLine1 = createTableSQLLine(referenceField, true, columnNameReference);
                    var format = format("CREATE TABLE %1$s (%2$s, %3$s);", tableName, tableSQLLine, tableSQLLine1);
                    stringBuilder.append(format);
                    stringBuilder.append("\n");
                    var template = "ALTER TABLE %1$s ADD FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s);";
                    var format1 = format(template,
                            tableName,
                            columnNameThis,
                            tableNameThis,
                            Mapper.getPrimaryKey(aClass)
                    );
                    stringBuilder.append(format1);
                    stringBuilder.append("\n");
                    var format2 = format(template,
                            tableName,
                            columnNameReference,
                            tableNameReference,
                            Mapper.getSQLFieldName(referenceField)
                    );
                    stringBuilder.append(format2);
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String alterTableAddForeignKeys(Class<? extends Savable> aClass) {
        var foreignKeyFields = Mapper.getFields(aClass, ForeignKey.class);
        var pairList = Arrays.stream(foreignKeyFields)
                .filter(field -> TypeMappings.getTypeName(field.getType()) != null)
                .filter(field -> field.isAnnotationPresent(ForeignKey.class))
                .map(field -> field.getAnnotation(ForeignKey.class).value().split("#"))
                .map(strings -> {
                    try {
                        return new Pair<>(Mapper.getTableName(strings[0]), strings[1]);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        var keys = pairList.stream().map(Pair::key).distinct().collect(Collectors.toList());
        StringBuilder stringBuilder = new StringBuilder();
        String tableName = aClass.getAnnotation(TableName.class).value();
        for (String key : keys) {
            var collect = pairList.stream().filter(pair -> pair.key().equals(key)).map(Pair::value).collect(Collectors.joining(", "));
            stringBuilder.append(
                    format("ALTER TABLE %1$s ADD %2$s REFERENCES %3$s(%4$s);",
                            tableName,
                            generateKeys(
                                    Mapper.getFields(aClass),
                                    ForeignKey.class,
                                    "FOREIGN KEY",
                                    false
                            ),
                            key,
                            collect
                    )
            );
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String createTableSQL(String tableName, Field[] fields) throws SQLFeatureNotSupportedException {
        var template = "CREATE TABLE %1$s (" +
                "%2$s" +
                ");";
        return format(template, tableName, createTableSQLLines(fields));
    }

    public String createTableSQL(Class<? extends Savable> aClass) throws SQLFeatureNotSupportedException {
        var annotation = aClass.getAnnotation(TableName.class);
        var fields = Mapper.getFields(aClass, field -> !field.isAnnotationPresent(Ignore.class));
        return createTableSQL(annotation.value(), fields);
    }

    private String createTableSQLLines(Field[] fields) throws SQLFeatureNotSupportedException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.getType().isArray()) {
                Class<?> componentType = field.getType().getComponentType();
                if (TypeMappings.getTypeName(componentType) == null) {
                    throw new SQLFeatureNotSupportedException("Arrays of type " + componentType.getSimpleName() + " are not allowed");
                }
                continue;
            }
            var tableSQLLine = createTableSQLLine(field);
            stringBuilder.append(tableSQLLine);
            stringBuilder.append(", ");
        }
        stringBuilder.append(generateKeys(fields, PrimaryKey.class, "PRIMARY KEY"));
        if (stringBuilder.length() > 0) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        }
        return stringBuilder.toString();
    }

    private String generateKeys(Field[] fields, Class<? extends Annotation> aClass, String prefix) {
        return generateKeys(fields, aClass, prefix, true);
    }

    private String generateKeys(Field[] fields, Class<? extends Annotation> aClass, String prefix, boolean withNext) {
        var primaryKeys = Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(aClass))
                .map(Mapper::getSQLFieldName).collect(Collectors.joining(", ", prefix + " (", ")" + (withNext ? ", " : "")));
        if (!primaryKeys.startsWith(prefix + " ()"))
            return primaryKeys;
        else return "";
    }

    private String createTableSQLLine(Field field) throws SQLFeatureNotSupportedException {
        return createTableSQLLine(field, false, null);
    }

    private String createTableSQLLine(Field field, boolean ignorePrimaryKey, String overrideName) throws SQLFeatureNotSupportedException {
        Class<?> type = field.getType();
        if (type.isArray()) {
            type = type.getComponentType();
        }
        var typeName = TypeMappings.getTypeName(type);
        if (field.isAnnotationPresent(PrimaryKey.class) && !ignorePrimaryKey) {
            if (field.getAnnotation(PrimaryKey.class).autogen()) {
                if (typeName.equals("BIGINT")) {
                    typeName = "BIGSERIAL";
                } else if (typeName.equals("INTEGER") || typeName.equals("SMALLINT")) {
                    typeName = "SERIAL";
                }
            }
        }
        if (typeName == null) {
            throw new SQLFeatureNotSupportedException("Could not lookup datatype of " + field.getName() + ": " + field.getType().getSimpleName());
        }
        var template = "%1$s %2$s";
        var name = overrideName == null ? Mapper.getSQLFieldName(field) : overrideName;
        var sql = format(template, name, typeName);
        if (field.isAnnotationPresent(Nullable.class)) {
            sql += " NULL";
        } else if (field.isAnnotationPresent(NotNull.class)) {
            sql += " NOT NULL";
        } else {
            sql += " NOT NULL";
        }
        if (field.isAnnotationPresent(Unique.class)) {
            sql += " UNIQUE";
        }
        return sql;
    }

    public String createInsertSQL(Class<? extends Savable> aClass) {
        var template = "INSERT INTO %1$s (%2$s) values (%3$s) returning *;";
        var insertedFields = Mapper.getInsertableFields(aClass);
        return format(template,
                Mapper.getTableName(aClass),
                Arrays.stream(insertedFields).map(Mapper::getSQLFieldName).collect(Collectors.joining(", ")),
                placeholders(insertedFields.length)
        );
    }

    public String createGetSQL(Class<? extends Savable> aClass) {
        var template = "SELECT * FROM %1$s WHERE %2$s";
        var annotation = aClass.getAnnotation(TableName.class);
        var tableName = annotation.value();
        var collect = Arrays.stream(Mapper.getFields(aClass, PrimaryKey.class)).map(this::createSQLWhere).collect(Collectors.joining(" and "));
        return format(template, tableName, collect);
    }

    public String createGetAllSQL(Class<? extends Savable> aClass) {
        var template = "SELECT * FROM %1$s";
        var annotation = aClass.getAnnotation(TableName.class);
        var tableName = annotation.value();
        return format(template, tableName);
    }

    private String createSQLWhere(Field field) {
        return format("%1$s = ?", Mapper.getSQLFieldName(field));
    }

    public String createRemoveSQL(Class<? extends Savable> aClass) {
        var template = "DELETE FROM %1$s WHERE %2$s returning *";
        var fields = Mapper.getFields(aClass, PrimaryKey.class);
        var collect = Arrays.stream(fields).map(this::createSQLWhere).collect(Collectors.joining(" and "));
        return format(template, aClass.getAnnotation(TableName.class).value(), collect);
    }

    public String createUpdateSQL(Class<? extends Savable> aClass) {
        var template = "UPDATE %1$s SET %2$s WHERE %3$s returning *";
        var primaryKeyFields = Mapper.getFields(aClass, PrimaryKey.class);
        var collectP = Arrays.stream(primaryKeyFields).map(this::createSQLWhere).collect(Collectors.joining(" and "));
        var insertableFields = Mapper.getInsertableFields(aClass);
        var collectF = Arrays.stream(insertableFields).map(this::createSQLWhere)
                .collect(Collectors.joining(", "));
        return format(template, aClass.getAnnotation(TableName.class).value(), collectF, collectP);
    }

}
