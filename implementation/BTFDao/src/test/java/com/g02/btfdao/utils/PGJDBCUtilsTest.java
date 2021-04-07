package com.g02.btfdao.utils;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.dao.DaoFactory;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.testentities.Dog;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Field;

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
}
