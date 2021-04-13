package com.g02.btfdao.mapper;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Mapper {

    private static final Map<Class<? extends Savable>, MapperInfo> mapperInfoMap = new ConcurrentHashMap<>();

    public static Field[] getFields(Class<? extends Savable> aClass) {
        return mapperInfoMap.computeIfAbsent(aClass, aClass1 -> {
            var fields = aClass1.getFields();
            var annotations = aClass1.getAnnotations();
            return new MapperInfo(Arrays.asList(annotations.clone()), Arrays.asList(fields));
        }).getFields().toArray(Field[]::new);
    }

    public static Field[] getFields(Class<? extends Savable> aClass, Class<? extends Annotation> annotation) {
        return Arrays.stream(getFields(aClass)).filter(field -> field.isAnnotationPresent(annotation)).toArray(Field[]::new);
    }

    public static Field[] getFields(Class<? extends Savable> aClass, Predicate<Field> predicate) {
        return Arrays.stream(getFields(aClass)).filter(predicate).toArray(Field[]::new);
    }

    public static <E extends Savable> List<Pair<String, Object>> deconstruct(E e) {
        var fields = getFields(e.getClass());
        return deconstruct(e, fields);
    }

    public static <E extends Savable> List<Pair<String, Object>> deconstructInsertableFields(E e) throws ClassNotFoundException, IllegalAccessException {
        var fields = getInsertableFields(e.getClass());
        var fields1 = Arrays.stream(fields)
                .filter(field ->
                        !(field.isAnnotationPresent(ForeignKey.class) && !field.getType().isArray() && isDatabaseType(field)))
                .toArray(Field[]::new);
        var deconstruct = deconstruct(e, fields1);
        var otherFields = getFields(e.getClass(), field -> field.isAnnotationPresent(ForeignKey.class) && !field.getType().isArray() && isDatabaseType(field));
        for (Field otherField : otherFields) {
            var o = otherField.get(e);
            System.out.println(o);
            Class<? extends Savable> referencingClass = getReferencingClass(otherField);
            var refPrimaryKeyFields = getFields(referencingClass, PrimaryKey.class);
            for (Field refPrimaryKeyField : refPrimaryKeyFields) {
                var o1 = refPrimaryKeyField.get(o);
                deconstruct.add(new Pair<>(refPrimaryKeyField.getName(), o1));
            }
        }
        return deconstruct;
    }

    public static <E extends Savable> List<Pair<String, Object>> deconstruct(E e, Field[] fields) {
        List<Pair<String, Object>> pairs = new ArrayList<>();
        for (Field field : fields) {
            try {
                var o = field.get(e);
                pairs.add(new Pair<>(field.getName(), o));
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return pairs;
    }

    public static <E extends Savable> Object[] getPrimaryKeyValues(E e) throws IllegalAccessException {
        var fields = getFields(e.getClass(), PrimaryKey.class);
        List<Object> objects = new ArrayList<>();
        for (Field field : fields) {
            var o = field.get(e);
            objects.add(o);
        }
        return objects.toArray();
    }

    public static <E extends Savable> E construct(Class<E> aClass, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        boolean hasNext = true;
        int i = 1;
        List<Object> objects = new ArrayList<>();
        while (hasNext) {
            try {
                var object = resultSet.getObject(i);
                objects.add(object);
            } catch (Exception throwables) {
                hasNext = false;
//                throwables.printStackTrace();
            }
            i++;
        }
        var fields = Arrays.stream(getFields(aClass, ForeignKey.class)).filter(f -> f.getType().isArray()).toArray(Field[]::new);

        return construct(aClass, objects.toArray());
    }

    public static <E extends Savable> E construct(Class<E> aClass, Object[] objects) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
//        var fields = getFields(aClass,a->!a.isAnnotationPresent(Ignore.class));
        System.out.println(Arrays.toString(objects));
        var fields = getConstructableFields(aClass);
        int index = 0;
        List<Object> list = new ArrayList<>();
        for (Field field : fields) {
            if (!field.getType().isArray() && field.isAnnotationPresent(ForeignKey.class) && isDatabaseType(field)) {
                Class<? extends Savable> type = (Class<? extends Savable>) field.getType();
                var length = getConstructableFields(type).length;
                list.add(construct(type, Arrays.copyOfRange(objects, index, index + length)));
                index += length;
            } else {
                list.add(objects[index]);
                index++;
            }
        }
        System.out.println(Arrays.toString(fields));
        var constructor = aClass.getDeclaredConstructor(Arrays.stream(fields).map(Field::getType).toArray(Class[]::new));
        var b = constructor.trySetAccessible();
        if (!b) throw new IllegalAccessException("Konnte Konstruktor nicht aufrufbar machen");
        var e = constructor.newInstance(list.toArray());
        System.out.println(e);
        return e;
    }

    public static String getSQLFieldName(Field field) {
        if (field.isAnnotationPresent(FieldName.class)) {
            return field.getAnnotation(FieldName.class).value();
        } else {
            return field.getName();
        }
    }

    public static String getTableName(String className) throws ClassNotFoundException {
        Class<?> aClass = Class.forName(className);
        return getTableName(aClass);
    }

    public static String getTableName(Class<?> aClass) {
        var annotation = aClass.getAnnotation(TableName.class);
        return annotation.value();
    }

    public static String getPrimaryKey(Class<? extends Savable> aClass) {
        var fields = getFields(aClass, PrimaryKey.class);
        return Arrays.stream(fields).map(Mapper::getSQLFieldName).collect(Collectors.joining(", "));
    }

    public static Field[] getInsertableFields(Class<? extends Savable> aClass) {
        var fields = getFields(aClass);
        return Arrays.stream(fields).filter(field -> {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                if (field.getAnnotation(PrimaryKey.class).autogen()) {
                    return false;
                }
            }
            if (field.isAnnotationPresent(Ignore.class)) return false;
            if (field.getType().isArray()) return false;
            return TypeMappings.getTypeName(field.getType()) != null || isDatabaseType(field);
        }).toArray(Field[]::new);
    }

    public static Field[] getConstructableFields(Class<? extends Savable> aClass) {
        var fields = getFields(aClass);
        return Arrays.stream(fields).filter(field -> {
            if (field.getType().isArray()) return false;
            if (field.isAnnotationPresent(Ignore.class)) return false;
            return TypeMappings.getTypeName(field.getType()) != null || isDatabaseType(field);
        }).toArray(Field[]::new);
    }

    public static boolean isValidDataType(Class<?> aClass) {
        return aClass.isArray() && TypeMappings.getTypeName(aClass.getComponentType()) != null;
    }

    public static String relationTableName(Field field1) throws ClassNotFoundException, NoSuchFieldException {
        var annotation = field1.getAnnotation(ForeignKey.class);
        var split = annotation.value().split("#");
        assert split.length == 2 : "ForeignKey reference has the wrong format";
        var referenceClass = Class.forName(split[0]);
        //var referenceField = referenceClass.getField(split[1]);
        var tableNameThis = Mapper.getTableName(field1.getDeclaringClass());
        var columnNameThis = tableNameThis + "_" + field1.getName();
        var tableNameReference = Mapper.getTableName(referenceClass);
        return columnNameThis + "_" + tableNameReference;
    }

    public static String relationColumnNames(Field field) throws ClassNotFoundException {
        var annotation = field.getAnnotation(ForeignKey.class);
        var split = annotation.value().split("#");
        assert split.length == 2 : "ForeignKey reference has the wrong format";
        var referenceClass = (Class<? extends Savable>) Class.forName(split[0]);

        var refPrimaryKeys = getFields(referenceClass, PrimaryKey.class);
        var thisClass = (Class<? extends Savable>) field.getDeclaringClass();

        var thisPrimaryKeys = getFields(thisClass, PrimaryKey.class);
        var tableNameThis = Mapper.getTableName(thisClass);

        var collect1 = Arrays.stream(thisPrimaryKeys).map(field1 -> tableNameThis + "_" + Mapper.getSQLFieldName(field1)).collect(Collectors.toList());
        ArrayList<String> allRelationTableFields = new ArrayList<>(collect1);
        var collect2 = Arrays.stream(refPrimaryKeys).map(field1 -> Mapper.getTableName(referenceClass) + "_" + Mapper.getSQLFieldName(field1)).collect(Collectors.toList());
        allRelationTableFields.addAll(collect2);
        return String.join(", ", allRelationTableFields);
    }

    public static String relationColumnLeftName(Field field) throws NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException { //TODO: Fix for multiple pks
        var annotation = field.getAnnotation(ForeignKey.class);
        var split = annotation.value().split("#");
        assert split.length == 2 : "ForeignKey reference has the wrong format";
        var referenceClass = Class.forName(split[0]);
        var tableNameThis = Mapper.getTableName(field.getDeclaringClass());
        var columnNameThis = tableNameThis + "_" + field.getName();
        return columnNameThis;
    }

    public static String relationColumnRightName(Field field) throws NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException { //TODO: Fix for multiple pks
        var annotation = field.getAnnotation(ForeignKey.class);
        var split = annotation.value().split("#");
        assert split.length == 2 : "ForeignKey reference has the wrong format";
        var referenceClass = Class.forName(split[0]);
        var referenceField = referenceClass.getField(split[1]);
        var tableNameThis = Mapper.getTableName(field.getDeclaringClass());
        var tableNameReference = Mapper.getTableName(referenceClass);
        var columnNameReference = tableNameReference + "_" + referenceField.getName();
        return columnNameReference;
    }

    public static boolean isDatabaseType(Field field) {
        var annotation = field.getAnnotation(ForeignKey.class);
        var className = annotation.value();
        try {
            var klasse = Class.forName(className.split("#")[0]);
            return Arrays.asList(klasse.getInterfaces()).contains(Savable.class);
        } catch (ClassNotFoundException e) {
            assert false : "Foreign Key falsch angegeben";
            return false;
        }
    }

    public static Class<? extends Savable> getReferencingClass(Field field) throws ClassNotFoundException {
        var annotation = field.getAnnotation(ForeignKey.class);
        return (Class<? extends Savable>) Class.forName(annotation.value().split("#")[0]);
    }
}
