package com.g02.btfdao.dao;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.annotations.TableName;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.ClientInfoStatus;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Dao<E extends Savable> {

    private final Connection connection;
    private final Class<E> entityClass;
    private final DaoFactory daoFactory;
    private final QueryExecutor queryExecutor;
    private final QueryBuilder queryBuilder;

    public Dao(Connection connection, Class<E> aClass, DaoFactory daoFactory) {
        this.connection = connection;
        entityClass = aClass;
        this.daoFactory = daoFactory;
        queryExecutor = new QueryExecutor();
        queryBuilder = new QueryBuilder();
    }

    @SafeVarargs
    public final List<E> insert(E... e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLException {
        return insert(Arrays.stream(e).collect(Collectors.toList()));
    }

    public List<E> insert(Collection<E> e) throws IllegalAccessException, SQLException, NoSuchFieldException, ClassNotFoundException {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            var opt=queryExecutor.doInsert(connection, queryBuilder.createInsertSQL(entityClass), entityClass, e1);
            if (!opt.isPresent()) continue;
            var e2=opt.get();
            var fields = Mapper.getFields(entityClass, ForeignKey.class);
//            System.out.println(e1);
            for (Field field : fields) {
                if (field.getType().isArray() && TypeMappings.getTypeName(field.getType().getComponentType()) != null) {
                    var o = field.get(e1); //Array
                    assert o.getClass().isArray();
                    var length=java.lang.reflect.Array.getLength(o);
                    for (int i = 0; i < length; i++) {
                        var a= Array.get(o,i);
                        var value = field.getAnnotation(ForeignKey.class).value();
                        String sql= format("insert into %1$s (%2$s) values (%3$s, %4$s) returning *",
                                Mapper.relationTableName(field),
                                Mapper.relationColumnNames(field),
                                Mapper.getPrimaryKeyValues(e2)[0],
                                a
                        );
                        var pst=connection.prepareStatement(sql);
                        pst.execute();
//                        System.out.println(sql);
                    }
                    field.set(e2,o);
                }
            }
            e2.afterConstruction();
            list.add(e2);
        }
        return list;
    }

    @SafeVarargs
    public final List<E> remove(E... e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        return remove(Arrays.stream(e).collect(Collectors.toList()));
    }

    public List<E> remove(Collection<E> e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            var a=get(Mapper.getPrimaryKeyValues(e1));
            if(a.isPresent()){
                for (Field field : Mapper.getFields(e1.getClass(), ForeignKey.class)) {
                    var sql=queryBuilder.createRelationRemoveSQL(field);
                    queryExecutor.doRemove(connection,sql,null,Mapper.getPrimaryKeyValues(a.get())[0]);
                }
                var b=queryExecutor.doRemove(connection, queryBuilder.createRemoveSQL(entityClass), entityClass, Mapper.getPrimaryKeyValues(a.get()));
                if (b.isPresent()) {
                    list.add(a.get());
                }
            }
        }
        return list;
    }

    public List<E> remove(Predicate<E> predicate) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        var list = get(predicate);
        return remove(list);
    }

    public E update(E e) throws IllegalAccessException, SQLException, NoSuchFieldException, ClassNotFoundException {
        queryExecutor.doUpdate(connection,queryBuilder.createUpdateSQL(entityClass),entityClass,e);
        for (Field field : Mapper.getFields(entityClass, ForeignKey.class)) {
            var sql=queryBuilder.createRelationRemoveSQL(field);
            queryExecutor.doRemove(connection,sql,null,Mapper.getPrimaryKeyValues(get(Mapper.getPrimaryKeyValues(e)).get())[0]);
            if (field.getType().isArray() && TypeMappings.getTypeName(field.getType().getComponentType()) != null) {
                var o = field.get(e); //Array
                assert o.getClass().isArray();
                var length=java.lang.reflect.Array.getLength(o);
                for (int i = 0; i < length; i++) {
                    var a= Array.get(o,i);
                    var value = field.getAnnotation(ForeignKey.class).value();
                    String sql2= format("insert into %1$s (%2$s) values (%3$s, %4$s) returning *",
                            Mapper.relationTableName(field),
                            Mapper.relationColumnNames(field),
                            Mapper.getPrimaryKeyValues(e)[0],
                            a
                    );
                    var pst=connection.prepareStatement(sql2);
                    pst.execute();
//                        System.out.println(sql);
                }
                field.set(e,o);
            }
        }
        return get(Mapper.getPrimaryKeyValues(e)).orElseGet(null);
    }

    public List<E> get(Predicate<E> predicate) {
        return getAll().stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Getting the object from the Database that has the given keyValues as its primary keys.
     * @param keyValues The values for the primary keys. Must provide as many as the Class has primary keys
     * @return
     */
    public Optional<E> get(Object... keyValues) {
        var expectedLength = Mapper.getFields(entityClass, PrimaryKey.class).length;
        var actualLength = keyValues.length;
        assert expectedLength == actualLength: "Expecting " + expectedLength + " keyValues, but got only " + actualLength;
        var getSQL = queryBuilder.createGetSQL(entityClass);
        var e = queryExecutor.doGet(connection, getSQL, entityClass, keyValues);
        e.ifPresent(Savable::afterConstruction);
        return e;
    }

    public Optional<E> getFirst(Predicate<E> predicate) {
        return getAll().stream().filter(predicate).findFirst();
    }

    public List<E> getAll() {
        var getSQL = queryBuilder.createGetAllSQL(entityClass);
        var e = queryExecutor.doGetAll(connection, getSQL, entityClass);
        for (E e1 : e) {
            e1.afterConstruction();
        }
        return Collections.unmodifiableList(e);
    }

}
