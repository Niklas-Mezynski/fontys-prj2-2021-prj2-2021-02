package com.g02.btfdao.utils;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.dao.DaoFactory;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.testentities.Cat;
import com.g02.btfdao.testentities.Dog;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.List;

public class PGJDBCUtilsTest {

    /*@Test
    void name() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var daoFactory = new DaoFactory(simpledao);
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.insert(new Dog(1, "Wuffy2","a"), new Dog(2, "Dogg","a"));
        wuffy2s.forEach(System.out::println);
    }*/

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
        Dog dog = new Dog(1, "Wuffy","a");
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
        var datebaseSQL = queryBuilder.createDatabaseSQL(new String[]{"com.g02.btfdao.testentities.Dog", "com.g02.btfdao.testentities.Cat"});
        System.out.println(datebaseSQL);
    }

    @Test
    void t6b() throws ClassNotFoundException, SQLFeatureNotSupportedException, NoSuchFieldException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDatabaseSQL(new String[]{"com.g02.btfdao.testentities.Test"});
        System.out.println(datebaseSQL);
    }

    @Test
    void t7() throws ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createDropSQL(new String[]{"com.g02.btfdao.testentities.Dog", "com.g02.btfdao.testentities.Cat"});
        System.out.println(datebaseSQL);
    }

    @Test
    void t8() throws ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createInsertSQL((Class<? extends Savable>) Class.forName("com.g02.btfdao.testentities.Dog"));
        System.out.println(datebaseSQL);
    }

    @Test
    void t9() throws ClassNotFoundException, SQLException {
        Dog dog = new Dog(1, "Wuffy","a");
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        System.out.println(queryExecutor.doInsert(con, queryBuilder.createInsertSQL(Dog.class), Dog.class, dog).get());
    }

    @Test
    void t10() {
        var queryBuilder = new QueryBuilder();
        var datebaseSQL = queryBuilder.createGetSQL(Dog.class);
        System.out.println(datebaseSQL);
    }

    @Test
    void t11() throws SQLException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        System.out.println(queryExecutor.doGet(con, queryBuilder.createInsertSQL(Dog.class), Dog.class, 11).get());
    }

    @Test
    void t12() throws SQLException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var daoFactory = new DaoFactory(simpledao);
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.get(11);
//        var wuffy2s = dao.insert(new Dog(1, "Wuffy2"), new Dog(2, "Dogg"));
        System.out.println(wuffy2s);
    }

    @Test
    void t13() throws SQLException, IllegalAccessException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var daoFactory = new DaoFactory(simpledao);
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.getAll();
//        var wuffy2s = dao.insert(new Dog(1, "Wuffy2"), new Dog(2, "Dogg"));
        System.out.println(wuffy2s);
    }

    @Test
    void t14() {
        var queryBuilder = new QueryBuilder();
        var removeSQL = queryBuilder.createRemoveSQL(Dog.class);
        System.out.println(removeSQL);
    }

    @Test
    void t15() throws ClassNotFoundException {
        var queryBuilder = new QueryBuilder();
        var removeSQL = queryBuilder.createUpdateSQL(Dog.class);
        System.out.println(removeSQL);
    }

    @Test
    void t16() throws SQLException, ClassNotFoundException, IllegalAccessException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.getAll().get(0);
        wuffy2s.name = "JHBACB";
        System.out.println(queryExecutor.doUpdate(con, queryBuilder.createInsertSQL(Dog.class), Dog.class, wuffy2s).get());
    }

    @Test
    void t17() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.get(dog -> dog.id > 6);
        var remove = dao.remove(wuffy2s);
        System.out.println(remove);
    }

    @Test
    void t18() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var daoFactory = new DaoFactory(simpledao);
        var dogDao = daoFactory.createDao(Dog.class);
        var catDao = daoFactory.createDao(Cat.class);
        var dog = new Dog(0, "Dog","a");
        var cat1 = new Cat(0, "Cat1");
        var cat2 = new Cat(0, "Cat2");
        var cat3 = new Cat(0, "Cat3");
        var cats = new Cat[]{cat1, cat2, cat3};
        dog.RealCats = cats;
        dog.buddy = new Cat(0, "Buddy");
        var insert = dogDao.insert(dog).get(0);
//        dog.cat = insert.stream().mapToInt(cat -> cat.catid).toArray();
        System.out.println(insert);
//        insert.name = "Test";
        System.out.println("Inserted: " + insert);
        insert.buddy.catname = "Buddy2";
        dogDao.update(insert);

    }

    @Test
    void t19() throws SQLException, IllegalAccessException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
//        var wuffy2s = dao.getAll().get(0);
//        wuffy2s.name = "JHBACB";
        System.out.println(dao.getAll());
    }
    @Test
    void t20() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
        var wuffy2s = dao.remove(new Dog(24,"", "D"));
        System.out.println(wuffy2s);
    }
    @Test
    void t21() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
        var catDao=daoFactory.createDao(Cat.class);
        var wuffy = new Dog(0,"Anatol", "LOL");
        var cat1 = new Cat(0, "Cat1");
        var cat2 = new Cat(0, "Cat2");
        var cat3 = new Cat(0, "Cat3");
        var cats = new Cat[]{cat1, cat2, cat3};
        var insert = catDao.insert(cats);
//        wuffy.cat = insert.stream().mapToInt(cat -> cat.catid).toArray();
        System.out.println(wuffy);
        wuffy=dao.insert(wuffy).get(0);
        System.out.println(wuffy);
        wuffy.name="Philip";
        wuffy=dao.update(wuffy).get();
        System.out.println(wuffy);
    }

    @Test
    void t22() throws ClassNotFoundException {
        var s = new QueryBuilder().alterTableAddForeignKeys(Dog.class);
    }

    @Test
    void t23() throws SQLException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        var con = simpledao.getConnection();
        var daoFactory = new DaoFactory(simpledao);
        var queryBuilder = new QueryBuilder();
        var queryExecutor = new QueryExecutor();
        var dao = daoFactory.createDao(Dog.class);
//        var wuffy2s = dao.getAll().get(0);
//        wuffy2s.name = "JHBACB";
        System.out.println(dao.remove(dao.get(2, "Dog").get()));
    }
}
