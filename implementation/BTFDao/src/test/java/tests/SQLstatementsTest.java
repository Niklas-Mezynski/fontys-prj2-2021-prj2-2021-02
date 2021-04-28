package tests;

import com.g02.btfdao.dao.Dao;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.dao.PGJDBCUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import testentities.Cat;
import testentities.Dog;
import testentities.Mouse;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class SQLstatementsTest {
    Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        var simpledao = PGJDBCUtils.getDataSource("simpledao");
        connection = simpledao.getConnection();
    }

    @Test
    void t01testGetStatement() {
        System.out.println(new QueryBuilder().createGetSQL(Dog.class));
        System.out.println(new QueryBuilder().createGetSQL(Cat.class));
        System.out.println(new QueryBuilder().createGetSQL(Mouse.class));
    }

    @Test
    void t02testCreateTable() {
        QueryBuilder.createDropStatements = true;
        String statements = new QueryBuilder().createTablesCreateStatement(List.of(Dog.class, Cat.class, Mouse.class));
        System.out.println(statements);
        new QueryExecutor().executeStatement(connection, statements, new Object[0]);
    }

    @Test
    void t03testInsertStatement() {
        System.out.println(new QueryBuilder().createInsertSQL(Dog.class));
    }

    @Test
    void t04testInsertAction() throws SQLException {
        Mouse mouse = new Mouse(0);
        Mouse mouse2 = new Mouse(1);
        Cat cat1 = new Cat(0, Collections.singletonList(mouse));
        Cat cat2 = new Cat(0, Collections.singletonList(mouse2));
        Dog dog = new Dog("Nils", cat1, 5, cat2);
        System.out.println(dog);
        Dao<Dog> dogDao = new Dao<>(Dog.class, connection);
        System.out.println(dogDao.insert(dog));
    }

    @Test
    void t05testGetAction() throws SQLException {
        Dao<Dog> dogdao = new Dao<>(Dog.class, connection);
        var dogopt = dogdao.get(new String[]{"Nils"});
        System.out.println(dogopt);
    }

    @Test
    void t06testGetAllAction() throws SQLException {
        Dao<Dog> dogdao = new Dao<>(Dog.class, connection);
        var dogopt = dogdao.getAll();
        System.out.println(dogopt);
    }

    @Test
    void t07testUpdateAction() throws SQLException {
        Dao<Dog> dogdao = new Dao<>(Dog.class, connection);
        var dogopt = dogdao.get(new String[]{"Nils"});
        var dog = dogopt.get();
        System.out.println(dog);
        dog.age = dog.age + 1;
        dog.buddy.getFood().get(0).setArraytest(new int[]{1, 3, 1, 2});
        dog = dogdao.update(dog);
        System.out.println(dog);
    }

    @Test
    void t08testRemoveAction() throws SQLException {
        Dao<Dog> dogdao = new Dao<>(Dog.class, connection);
        var dogopt = dogdao.get(new String[]{"Nils"});
        var dog = dogopt.get();
        System.out.println(dog);
        dogdao.remove(dog, false);
        dogopt = dogdao.get(new String[]{"Nils"});
        System.out.println(dogopt);
    }
}
