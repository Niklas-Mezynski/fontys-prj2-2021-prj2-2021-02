package com.g02.btfdao.dao;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.queries.QueryBuilder;
import com.g02.btfdao.queries.QueryExecutor;
import com.g02.btfdao.utils.Savable;
import com.g02.btfdao.utils.TypeMappings;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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

    /*public final E insert(E e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLException {
//        return insert();
        return null;
    }
//*/
    public final List<E> insert(Savable... e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLException {
        return insert(Arrays.stream(e).collect(Collectors.toList()));
    }

    public List<E> insert(Collection<? extends Savable> e) throws IllegalAccessException, SQLException, NoSuchFieldException, ClassNotFoundException {
        List<E> list = new ArrayList<>();
        for (Savable savable : e) {
            var e1 = (E) savable;
            System.out.println(e1);
            var fields = Mapper.getFields(entityClass, ForeignKey.class);
            for (Field field : fields) {
                if (!field.getType().isArray() && Mapper.isDatabaseType(field)) {
                    var o = (Savable) field.get(savable);
                    Dao<? extends Savable> dao = daoFactory.createDao((Class<? extends Savable>) o.getClass());
                    var insert = dao.insert(o).get(0);
                    System.out.println("inserted1: " + insert);
                    field.set(savable, insert);
                }
            }
            var opt = queryExecutor.doInsert(connection, queryBuilder.createInsertSQL(entityClass), entityClass, e1);
            if (!opt.isPresent()) continue;
            var e2 = opt.get();
            System.out.println("e2: " + e2);
            for (Field field : fields) {
                if (!field.getType().isArray() && Mapper.isDatabaseType(field)) {
                    var o = (Savable) field.get(savable);
                    Dao<? extends Savable> dao = daoFactory.createDao((Class<? extends Savable>) o.getClass());
                    /*if (dao.get(Mapper.getPrimaryKeyValues(o)).isPresent()) {
                        continue;
                    }*/
                    var insert = dao.insert(o).get(0);
                    System.out.println("inserted1: " + insert);
                    field.set(e2, insert);
                }
            }
            for (Field field : fields) {
                if (field.getType().isArray()) {
                    if (TypeMappings.getTypeName(field.getType().getComponentType()) != null) {
                        var o = field.get(e1); //Array
                        assert o.getClass().isArray();
                        var length = Array.getLength(o);
                        for (int i = 0; i < length; i++) {
                            var a = Array.get(o, i);
                            var value = field.getAnnotation(ForeignKey.class).value();
                            String sql = format("insert into %1$s (%2$s) values (%3$s, %4$s) on conflict (*) do update returning *",
                                    Mapper.relationTableName(field),
                                    Mapper.relationColumnNames(field),
                                    Mapper.getPrimaryKeyValues(e2)[0],
                                    a
                            );
                            var pst = connection.prepareStatement(sql);
                            pst.execute();
//                        System.out.println(sql);
                        }
                        field.set(e2, o);
                    } else if (Mapper.isDatabaseType(field)) {
                        var savables = (Savable[]) field.get(e1);
                        var insertList = new ArrayList<E>();
                        System.out.println("sav: " + savables);
                        for (Savable savable1 : savables) {
                            Class<? extends Savable> type = savable1.getClass();
                            Dao<? extends Savable> dao = daoFactory.createDao(type);
                            System.out.println(savable1);
                            var ins = dao.insert(savable1);
                            insertList.addAll((Collection<E>) ins);

                            String sql = format("insert into %1$s (%2$s) values (%3$s, %4$s) returning *;",
                                    Mapper.relationTableName(field),
                                    Mapper.relationColumnNames(field),
                                    Arrays.stream(Mapper.getPrimaryKeyValues(e2)).map(o -> "?").collect(Collectors.joining(", ")),
                                    Arrays.stream(Mapper.getPrimaryKeyValues(ins.get(0))).map(o -> "?").collect(Collectors.joining(", "))
                            );
                            System.out.println(sql);

                            var pst = connection.prepareStatement(sql);

                            var length = Mapper.getPrimaryKeyValues(e2).length;
                            var length1 = Mapper.getPrimaryKeyValues(ins.get(0)).length;
                            var lengthNew = length + length1;
                            var objects = new Object[lengthNew];
                            for (int i = 0; i < length; i++) {
                                objects[i] = Mapper.getPrimaryKeyValues(e2)[i];
                            }
                            for (int i = 0; i < length1; i++) {
                                objects[i + length] = Mapper.getPrimaryKeyValues(ins.get(0))[i];
                            }
                            queryExecutor.fillPreparedStatement(pst, objects);

                            pst.execute();

                        }
                        System.out.println("InsertedList: " + insertList);
                        var o = Array.newInstance(field.getType().getComponentType(), insertList.size());
                        for (int i = 0; i < insertList.size(); i++) {
                            Array.set(o, i, insertList.get(i));
                        }
                        field.set(e2, o);
                    }
                }
            }
            e2.afterConstruction();
            list.add(e2);
        }
        return list;
    }

    /*public List<E> insert(Savable... cast) throws ClassNotFoundException, SQLException, NoSuchFieldException, IllegalAccessException {
        return insert((Collection<E>) List.of(cast));
    }*/

    @SafeVarargs
    public final List<E> remove(E... e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        return remove(Arrays.stream(e).collect(Collectors.toList()));
    }

    public List<E> remove(Collection<E> e) throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException, SQLFeatureNotSupportedException {
        List<E> list = new ArrayList<>();
        for (E e1 : e) {
            var a = get(Mapper.getPrimaryKeyValues(e1));
            if (a.isPresent()) {
                for (Field field : Mapper.getFields(e1.getClass(), ForeignKey.class)) {
                    if (field.getType().isArray()) {
                        var sql = queryBuilder.createRelationRemoveSQL(field);
                        queryExecutor.doRemove(connection, sql, null, Mapper.getPrimaryKeyValues(a.get()));
                    }
                }
                var b = queryExecutor.doRemove(connection, queryBuilder.createRemoveSQL(entityClass), entityClass, Mapper.getPrimaryKeyValues(a.get()));
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

    public Optional<E> update(Savable e) throws IllegalAccessException, SQLException, NoSuchFieldException, ClassNotFoundException {
        for (Field field : Mapper.getFields(entityClass, ForeignKey.class)) {
            var type = field.getType();
            if (type.isArray() && TypeMappings.getTypeName(type.getComponentType()) != null) {
                var sql = queryBuilder.createRelationRemoveSQL(field);
                System.out.println(sql);
                queryExecutor.doRemove(connection, sql, null, Mapper.getPrimaryKeyValues(e));
                var o = field.get(e); //Array
                assert o.getClass().isArray();
                var length = java.lang.reflect.Array.getLength(o);
                for (int i = 0; i < length; i++) {
                    var a = Array.get(o, i);
                    var value = field.getAnnotation(ForeignKey.class).value();
                    String sql2 = format("insert into %1$s (%2$s) values (%3$s, %4$s) returning *",
                            Mapper.relationTableName(field),
                            Mapper.relationColumnNames(field),
                            Mapper.getPrimaryKeyValues(e)[0],
                            a
                    );
                    var pst = connection.prepareStatement(sql2);
                    pst.execute();
//                        System.out.println(sql);
                }
                field.set(e, o);
            } else if (Mapper.isDatabaseType(field) && !field.getType().isArray()) {
                var typeS = (Class<? extends Savable>) type;
                Dao<? extends Savable> dao = daoFactory.createDao(typeS);
                Optional<? extends Savable> update = dao.update((Savable) field.get(e));
                System.out.println("Updated: " + update);
            }
        }
        var e1 = (E) e;
        System.out.println(e);
        queryExecutor.doUpdate(connection, queryBuilder.createUpdateSQL(entityClass), entityClass, e1);
        return get(Mapper.getPrimaryKeyValues(e));
    }

    public List<E> get(Predicate<E> predicate) throws IllegalAccessException {
        return getAll().stream().filter(predicate).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Getting the object from the Database that has the given keyValues as its primary keys.
     *
     * @param keyValues The values for the primary keys. Must provide as many as the Class has primary keys
     * @return
     */
    public Optional<E> get(Object... keyValues) {
        var expectedLength = Mapper.getFields(entityClass, PrimaryKey.class).length;
        var actualLength = keyValues.length;
        assert expectedLength == actualLength : "Expecting " + expectedLength + " keyValues, but got only " + actualLength;
        var getSQL = queryBuilder.createGetSQL(entityClass);
        var e = queryExecutor.doGet(connection, getSQL, entityClass, keyValues);
        e.ifPresent(Savable::afterConstruction);
        return e;
    }

    public Optional<E> getFirst(Predicate<E> predicate) throws IllegalAccessException {
        return getAll().stream().filter(predicate).findFirst();
    }

    public List<E> getAll() throws IllegalAccessException {
        var getSQL = queryBuilder.createGetAllSQL(entityClass);
        var e = queryExecutor.doGetAll(connection, getSQL, entityClass);
        var returnList = new ArrayList<E>();
        for (E e1 : e) {
            get(Mapper.getPrimaryKeyValues(e1)).ifPresent(returnList::add);
        }
        return Collections.unmodifiableList(returnList);
    }

}
