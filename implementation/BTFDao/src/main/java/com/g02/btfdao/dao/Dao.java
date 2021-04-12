package com.g02.btfdao.dao;

import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.utils.Savable;

import java.sql.ClientInfoStatus;
import java.sql.Connection;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Dao<E extends Savable> {

    private final Connection connection;
    private final Class<E> entityClass;
    private final QueryExecutor queryExecutor;
    private final QueryBuilder queryBuilder;

    public Dao(Connection connection, Class<E> aClass) {
        this.connection = connection;
        entityClass = aClass;
        queryExecutor = new QueryExecutor();
        queryBuilder = new QueryBuilder();
    }

    @SafeVarargs
    public final List<E> insert(E... e) {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            queryExecutor.doInsert(connection, queryBuilder.createInsertSQL(entityClass), entityClass, e1).ifPresent(list::add);
        }
        return list;
    }

    public List<E> insert(Collection<E> e) {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            queryExecutor.doInsert(connection, queryBuilder.createInsertSQL(entityClass), entityClass, e1).ifPresent(list::add);
        }
        return list;
    }

    @SafeVarargs
    public final List<E> remove(E... e) throws IllegalAccessException {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            queryExecutor.doRemove(connection, queryBuilder.createRemoveSQL(entityClass), entityClass, Mapper.getPrimaryKeyValues(e1)).ifPresent(list::add);
        }
        return list;
    }

    public List<E> remove(Collection<E> e) throws IllegalAccessException {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            queryExecutor.doRemove(connection, queryBuilder.createRemoveSQL(entityClass), entityClass, Mapper.getPrimaryKeyValues(e1)).ifPresent(list::add);
        }
        return list;
    }

    public List<E> remove(Predicate<E> predicate) throws IllegalAccessException {
        var list = get(predicate);
        return remove(list);
    }

    public E update(E e) throws IllegalAccessException {
        return queryExecutor.doRemove(connection, queryBuilder.createRemoveSQL(entityClass), entityClass, Mapper.getPrimaryKeyValues(e)).get();
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
        return e;
    }

    public Optional<E> getFirst(Predicate<E> predicate) {
        return getAll().stream().filter(predicate).findFirst();
    }

    public List<E> getAll() {
        var getSQL = queryBuilder.createGetAllSQL(entityClass);
        var e = queryExecutor.doGetAll(connection, getSQL, entityClass);
        return Collections.unmodifiableList(e);
    }

}
