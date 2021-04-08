package com.g02.btfdao.utils;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.dao.DaoFactory;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.testentities.Dog;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLFeatureNotSupportedException;

public class PGJDBCUtilsTest {

    @Test
    void name() {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var daoFactory = new DaoFactory(simpledao);
        var dao = daoFactory.createDao(Dog.class);
    }

    @Test
    void t2() {
        var fields = Mapper.getFields(Dog.class);
        for (Field field : fields) {
            System.out.println(field.toString());
        }
    }

    @Test
    void t3() {
        var fields = Mapper.getFields(Dog.class, PrimaryKey.class);
        for (Field field : fields) {
            System.out.println(field);
        }
    }

    @Test
    void t4() {
        Dog dog = new Dog();
        dog.name = "Wuffy";
        dog.id = 1;
        var deconstruct = Mapper.deconstruct(dog);
        deconstruct.forEach(pair -> {
            System.out.println(pair.key() + ": " + pair.value());
        });
    }

    @Test
    void t5() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Object[] objects = new Object[]{
                1,
                "Wuff"
        };
        Mapper.construct(Dog.class, objects);
    }

    @Test
    void t6() throws ClassNotFoundException, SQLFeatureNotSupportedException, NoSuchFieldException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDatabaseSQL(new String[]{"com.g02.btfdao.testentities.Dog","com.g02.btfdao.testentities.Cat"});
        System.out.println(datebaseSQL);
    }

    @Test
    void t7() throws ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDropSQL(new String[]{"com.g02.btfdao.testentities.Dog","com.g02.btfdao.testentities.Cat"});
        System.out.println(datebaseSQL);
    }
}
