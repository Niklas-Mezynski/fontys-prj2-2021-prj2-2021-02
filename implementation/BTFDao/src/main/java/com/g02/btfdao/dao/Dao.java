package com.g02.btfdao.dao;

import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.utils.Savable;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
            queryExecutor.doInsert(connection, queryBuilder.createInsertSQL(entityClass), e1).ifPresent(list::add);
        }
        return list;
    }

    List<E> insert(Collection<E> e) {
        return null;
    }

    @SafeVarargs
    final List<E> remove(E... e) {
        return null;
    }

    List<E> remove(Collection<E> e) {
        return null;
    }

    List<E> remove(Predicate<E> predicate) {
        return null;
    }

    E update(E e) {
        return null;
    }

    List<E> get(Predicate<E> predicate) {
        return null;
    }

    E getFirst(Predicate<E> predicate) {
        return null;
    }

    List<E> getAll() {
        return get(x -> true);
    }

}
