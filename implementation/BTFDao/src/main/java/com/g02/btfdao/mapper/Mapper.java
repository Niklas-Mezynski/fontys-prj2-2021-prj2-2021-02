package com.g02.btfdao.mapper;

import com.g02.btfdao.utils.Savable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public static <E extends Savable> Object[] deconstruct(E e) {
        return null;
    }

    public static <E extends Savable> E construct(Class<E> aClass, ResultSet resultSet) {
        return null;
    }

}
