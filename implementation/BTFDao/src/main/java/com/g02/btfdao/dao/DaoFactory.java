package com.g02.btfdao.dao;

import com.g02.btfdao.utils.Savable;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class DaoFactory {

    private final DataSource dataSource;

    private final HashMap<Class<? extends Savable>, Dao<? extends Savable>> classHashMap;

    public DaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
        classHashMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public <E extends Savable> Dao<E> createDao(Class<E> aClass) {
        Dao<?> dao = classHashMap.computeIfAbsent(aClass, aClass1 -> {
            try {
                Class<?> daoClass = Class.forName(aClass.getName() + "Dao");
                return (Dao<?>) daoClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
        return (Dao<E>) dao;
    }
}
