package com.g02.btfdao.mapper;

import com.g02.btfdao.annotations.*;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static <E extends Savable> List<Pair<String, Object>> deconstructInsertableFields(E e) {
        var fields = getInsertableFields(e.getClass());
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
        var fields= Arrays.stream(getFields(aClass,ForeignKey.class)).filter(f->f.getType().isArray()).toArray(Field[]::new);

        return construct(aClass, objects.toArray());
    }

    public static <E extends Savable> E construct(Class<E> aClass, Object[] objects) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
//        var fields = getFields(aClass,a->!a.isAnnotationPresent(Ignore.class));
        var fields= getConstructableFields(aClass);
        var constructor = aClass.getConstructor(Arrays.stream(fields).map(Field::getType).toArray(Class[]::new));
        var e = constructor.newInstance(objects);
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
        var fields=getFields(aClass);
        return Arrays.stream(fields).filter(field->{
            if (field.isAnnotationPresent(PrimaryKey.class)){
                if (field.getAnnotation(PrimaryKey.class).autogen()){
                    return false;
                }
            }
            if (field.isAnnotationPresent(Ignore.class))return false;
            if (field.getType().isArray()) return false;
            return TypeMappings.getTypeName(field.getType()) != null;
        }).toArray(Field[]::new);
    }

    public static Field[] getConstructableFields(Class<? extends Savable> aClass) {
        var fields=getFields(aClass);
        return Arrays.stream(fields).filter(field->{
            if (field.getType().isArray()) return false;
            if (field.isAnnotationPresent(Ignore.class))return false;
            return TypeMappings.getTypeName(field.getType()) != null;
        }).toArray(Field[]::new);
    }

}
