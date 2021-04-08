package com.g02.btfdao.dao;

import com.g02.btfdao.utils.Savable;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;

public class DaoFactory {

    private final DataSource dataSource;

    private final HashMap<Class<? extends Savable>, Dao<? extends Savable>> classHashMap;

    public DaoFactory(DataSource dataSource) {
        this.dataSource = dataSource;
        classHashMap = new HashMap<>();
    }

    public <E extends Savable> Dao<E> createDao(Class<E> aClass) throws SQLException {
        /*Dao<?> dao = classHashMap.computeIfAbsent(aClass, aClass1 -> {
            try {
                Class<?> daoClass = Class.forName(aClass.getName() + "Dao");
                return (Dao<?>) daoClass.getConstructor().newInstance();
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        });
        return (Dao<E>) dao;*/
        return new Dao<>(dataSource.getConnection(), aClass);
    }
}
