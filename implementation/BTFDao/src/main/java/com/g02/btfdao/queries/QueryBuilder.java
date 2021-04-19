package com.g02.btfdao.queries;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.g02.btfdao.mapper.Mapper.*;
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
                    if (TypeMappings.getTypeName(field.getType().getComponentType()) == null) {
                        var tableName = aClass.getAnnotation(TableName.class).value();
                        var split = field.getAnnotation(ForeignKey.class).value().split("#");
                        var refTableName = split[0];
                        var refClass = (Class<? extends Savable>) Class.forName(refTableName);
                        var refPrimaryKeys = Mapper.getFields(refClass, PrimaryKey.class);
                        var refFieldNames = Arrays.stream(refPrimaryKeys).map(Mapper::getSQLFieldName).collect(Collectors.toList());

                        var relationTableName = tableName + "_" + field.getName() + "_" + Mapper.getTableName(refClass);

                        var thisPrimaryKeys = Mapper.getFields(aClass, PrimaryKey.class);
                        var thisFieldNames = Arrays.stream(thisPrimaryKeys).map(Mapper::getSQLFieldName).collect(Collectors.toList());


                        var collect1 = Arrays.stream(thisPrimaryKeys).map(field1 -> {
                            try {
                                return createTableSQLLine(field1, true, tableName + "_" + Mapper.getSQLFieldName(field1));
                            } catch (SQLFeatureNotSupportedException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            return null;
                        }).collect(Collectors.toList());
                        ArrayList<String> allRelationTableFields = new ArrayList<>(collect1);
                        var collect2 = Arrays.stream(refPrimaryKeys).map(field1 -> {
                            try {
                                return createTableSQLLine(field1, true, Mapper.getTableName(refClass) + "_" + Mapper.getSQLFieldName(field1));
                            } catch (SQLFeatureNotSupportedException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            return null;
                        }).collect(Collectors.toList());
                        allRelationTableFields.addAll(collect2);

                        var format1 = format("CREATE TABLE %s (%s);",
                                relationTableName,
                                String.join(", ", allRelationTableFields)
                        );
                        stringBuilder.append(format1);
                        stringBuilder.append("\n");

                        var format2 = format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                                relationTableName,
                                thisFieldNames.stream().map(s -> Mapper.getTableName(aClass) + "_" + s).collect(Collectors.joining(", ")),
                                Mapper.getTableName(aClass),
                                String.join(", ", thisFieldNames));
                        stringBuilder.append(format2);
                        stringBuilder.append("\n");

                        var format = format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                                relationTableName,
                                refFieldNames.stream().map(s -> Mapper.getTableName(refClass) + "_" + s).collect(Collectors.joining(", ")),
                                Mapper.getTableName(refTableName),
                                String.join(", ", refFieldNames));
                        stringBuilder.append(format);

                    } else {
                        var annotation = field.getAnnotation(ForeignKey.class);
                        var split = annotation.value().split("#");
                        assert split.length == 2 : "ForeignKey reference has the wrong format";
                        var referenceClass = Class.forName(split[0]);
                        var referenceField = referenceClass.getField(split[1]);
                        var tableNameThis = Mapper.getTableName(aClass);
                        var columnNameThis = tableNameThis + "_" + field.getName();
                        var tableNameReference = Mapper.getTableName(referenceClass);
                        var columnNameReference = tableNameReference + "_" + referenceField.getName();
//                    var tableName = columnNameThis + "_" + columnNameReference;
                        var tableName = Mapper.relationTableName(field);
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
                    }
                    stringBuilder.append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public String alterTableAddForeignKeys(Class<? extends Savable> aClass) throws ClassNotFoundException {
        var foreignKeyFields = Mapper.getFields(aClass, ForeignKey.class);
        var fieldsSQLDataType = Arrays.stream(foreignKeyFields)
                .filter(field -> TypeMappings.getTypeName(field.getType()) != null)
                .collect(Collectors.toList());
        var fieldsDataType = Arrays.stream(foreignKeyFields)
                .filter(field -> TypeMappings.getTypeName(field.getType()) == null)
                .filter(Mapper::isDatabaseType)
                .filter(field -> !field.getType().isArray())
                .collect(Collectors.toList());
        var tableName = aClass.getAnnotation(TableName.class).value();
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fieldsSQLDataType) {
            var split = field.getAnnotation(ForeignKey.class).value().split("#");
            var refTableName = split[0];
            var refFieldName = split[1];
            var format = format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                    tableName,
                    Mapper.getSQLFieldName(field),
                    Mapper.getTableName(refTableName),
                    refFieldName);
            stringBuilder.append(format);
            stringBuilder.append("\n");
        }
        for (Field field : fieldsDataType) {
            var split = field.getAnnotation(ForeignKey.class).value().split("#");
            var refTableName = split[0];
            var refClass = (Class<? extends Savable>) Class.forName(refTableName);
            var refPrimaryKeys = Mapper.getFields(refClass, PrimaryKey.class);
            var refFieldNames = Arrays.stream(refPrimaryKeys).map(Mapper::getSQLFieldName).collect(Collectors.toList());
            var format = format("ALTER TABLE %s ADD FOREIGN KEY (%s) REFERENCES %s(%s);",
                    tableName,
                    refFieldNames.stream().map(s -> Mapper.getSQLFieldName(field) + "_" + s).collect(Collectors.joining(", ")),
                    Mapper.getTableName(refTableName),
                    String.join(", ", refFieldNames));
            stringBuilder.append(format);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String createTableSQL(String tableName, Field[] fields) throws SQLFeatureNotSupportedException, ClassNotFoundException {
        var template = "CREATE TABLE %1$s (" +
                "%2$s" +
                ");";
        return format(template, tableName, createTableSQLLines(fields));
    }

    public String createTableSQL(Class<? extends Savable> aClass) throws SQLFeatureNotSupportedException, ClassNotFoundException {
        var annotation = aClass.getAnnotation(TableName.class);
        var fields = Mapper.getFields(aClass, field -> !field.isAnnotationPresent(Ignore.class));
        return createTableSQL(annotation.value(), fields);
    }

    private String createTableSQLLines(Field[] fields) throws SQLFeatureNotSupportedException, ClassNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            if (field.getType().isArray()) {
                Class<?> componentType = field.getType().getComponentType();
                if (TypeMappings.getTypeName(componentType) == null) {
                    if (field.isAnnotationPresent(ForeignKey.class)) {
                        String fk = field.getAnnotation(ForeignKey.class).value();
                        String className = fk.split("#")[0];
                        Class<?> klasse = Class.forName(className);
                        if (Arrays.asList(klasse.getInterfaces()).contains(Savable.class)) {

                        } else {
                            throw new SQLFeatureNotSupportedException("Arrays of type " + componentType.getSimpleName() + " are not Savable");
                        }
                    } else {
                        throw new SQLFeatureNotSupportedException("Arrays of type " + componentType.getSimpleName() + " are not allowed");
                    }
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
        var names = Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(aClass))
                .filter(field -> !field.isAnnotationPresent(ForeignKey.class))
                .map(Mapper::getSQLFieldName)
                .collect(Collectors.toList());
        var collect = Arrays.stream(fields)
                .filter(field -> field.isAnnotationPresent(ForeignKey.class))
                .filter(field -> field.isAnnotationPresent(PrimaryKey.class))
                .map(field -> {
                    Class<? extends Savable> classa = null;
                    try {
                        classa = (Class<? extends Savable>) Class.forName(field.getAnnotation(ForeignKey.class).value().split("#")[0]);
                        var fields2 = getFields(classa, PrimaryKey.class);
                        return Arrays.stream(fields2)
                                .map(field1 -> field.getName() + "_" + field1.getName())
                                .collect(Collectors.toList());
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        names.addAll(collect);
        var primaryKeys = names.stream()
                .collect(Collectors.joining(", ", prefix + " (", ")" + (withNext ? ", " : "")));
        if (!primaryKeys.startsWith(prefix + " ()"))
            return primaryKeys;
        else return "";
    }

    private String createTableSQLLine(Field field) throws SQLFeatureNotSupportedException, ClassNotFoundException {
        return createTableSQLLine(field, false, null);
    }

    private String createTableSQLLine(Field field, boolean ignorePrimaryKey, String overrideName) throws SQLFeatureNotSupportedException, ClassNotFoundException {
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
        if (typeName == null && !isDatabaseType(field)) {
            throw new SQLFeatureNotSupportedException("Could not lookup datatype of " + field.getName() + ": " + field.getType().getSimpleName());
        }
        if (typeName == null && isDatabaseType(field)) {
            Class<? extends Savable> classa = (Class<? extends Savable>) Class.forName(field.getAnnotation(ForeignKey.class).value().split("#")[0]);
            var fields = Mapper.getFields(classa, PrimaryKey.class);
            String ret = "";
            for (Field field1 : fields) {
                var oname = field1.isAnnotationPresent(FieldName.class) ? field1.getAnnotation(FieldName.class).value() : null;
                var a = createTableSQLLine(field1, true, field.getName() + "_" + field1.getName());
                System.out.println("--" + a);
                ret = ret + a + ", ";
            }
            return ret.substring(0, ret.length() - 2);
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

    public String createInsertSQL(Class<? extends Savable> aClass) throws ClassNotFoundException {
        var template = "INSERT INTO %1$s (%2$s) values (%3$s) returning *;";
        var insertedFields = Mapper.getInsertableFields(aClass);
        var finalFields = Arrays.stream(insertedFields)
                .filter(field ->
                        !field.isAnnotationPresent(ForeignKey.class) || field.getType().isArray() || !isDatabaseType(field))
                .map(Mapper::getSQLFieldName)
                .collect(Collectors.toList());
        var inflate = Arrays.stream(insertedFields)
                .filter(field ->
                        field.isAnnotationPresent(ForeignKey.class) && !field.getType().isArray() && isDatabaseType(field))
                .collect(Collectors.toList());
        for (Field field : inflate) {
            Class<? extends Savable> referencingClass = getReferencingClass(field);
            var fields = getFields(referencingClass, PrimaryKey.class);
            for (Field field1 : fields) {
                var s = getSQLFieldName(field) + "_" + field1.getName();
                finalFields.add(s);
            }
        }
        System.out.println(aClass);
        return format(template,
                Mapper.getTableName(aClass),
                String.join(", ", finalFields),
                placeholders(finalFields.size())
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

    public String createUpdateSQL(Class<? extends Savable> aClass) throws ClassNotFoundException {
        var template = "UPDATE %1$s SET %2$s WHERE %3$s returning *";
        var primaryKeyFields = Mapper.getFields(aClass, PrimaryKey.class);
        var collectP = Arrays.stream(primaryKeyFields).map(this::createSQLWhere).collect(Collectors.joining(" and "));
        var insertedFields = Mapper.getInsertableFields(aClass);
        var finalFields = Arrays.stream(insertedFields)
                .filter(field ->
                        !field.isAnnotationPresent(ForeignKey.class) || field.getType().isArray() || !isDatabaseType(field))
                .map(Mapper::getSQLFieldName)
                .collect(Collectors.toList());
        var inflate = Arrays.stream(insertedFields)
                .filter(field ->
                        field.isAnnotationPresent(ForeignKey.class) && !field.getType().isArray() && isDatabaseType(field))
                .collect(Collectors.toList());
        for (Field field : inflate) {
            Class<? extends Savable> referencingClass = getReferencingClass(field);
            var fields = getFields(referencingClass, PrimaryKey.class);
            for (Field field1 : fields) {
                var s = getSQLFieldName(field) + "_" + field1.getName();
                finalFields.add(s);
            }
        }

        return format(template, aClass.getAnnotation(TableName.class).value(), finalFields.stream().map(s -> s + " = ?").collect(Collectors.joining(", ")), collectP);
    }

    public String createRelationRemoveSQL(Field field) throws NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        var template = "DELETE FROM %1$s where %2$s;";
        var tableName = Mapper.relationTableName(field);
        var sql = format(template,
                tableName,
                Mapper.relationColumnLeftName(field, " = ? and ") + " = ?"
        );
        return sql;
    }


}
