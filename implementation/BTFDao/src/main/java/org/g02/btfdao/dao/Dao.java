package org.g02.btfdao.dao;

import org.g02.btfdao.queries.QueryBuilder;
import org.g02.btfdao.queries.QueryExecutor;
import org.g02.btfdao.utils.Mapper;
import org.g02.btfdao.utils.SQLField;
import org.postgresql.jdbc.PgArray;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Dao<E extends Savable> {
    //private Connection connection;
    private Class<E> entityType;
    private List<SQLField> fields;
    private QueryBuilder queryBuilder;
    private QueryExecutor queryExecutor;
    private boolean followOnUpdate = true;
    private final DataSource dataSource;

    @Deprecated //will use default dataSource, Connection parameter is ignored
    public Dao(Class<E> entityType, Connection connection) {
        this(entityType, PGJDBCUtils.getDataSource("simpledao"));
    }

    public Dao(Class<E> entityType, DataSource dataSource) {
        this.entityType = entityType;
        fields = Mapper.getAllSQLFields(entityType);
        this.queryBuilder = new QueryBuilder();
        this.queryExecutor = new QueryExecutor();
        this.dataSource = dataSource;
//        try {
//            this.connection=dataSource.getConnection();
//            this.connection.close();
//        } catch (SQLException throwables) {
//            throw new RuntimeException(throwables);
//        }
    }

    public static void createAllTables(Connection connection, List<Class<? extends Savable>> classes) {
        var qb = new QueryBuilder();
        QueryBuilder.createDropStatements = true;
        var qe = new QueryExecutor();
        qe.executeStatement(connection, qb.createTablesCreateStatement(classes), new Object[0]);
    }

    public List<E> getAll() throws SQLException {
        //aquire Connection
        var connection = dataSource.getConnection();
        List<E> ret;
        String sql = queryBuilder.createGetAllSQL(entityType);
        ResultSet rst = queryExecutor.executeQuery(connection, sql, new Object[0]);
        ExecutorService es = Executors.newCachedThreadPool();

        var map = new ConcurrentHashMap<Integer, Object>();
        var list = new ArrayList<Object[]>();

        while (rst.next()) { //Read out pks
            var array = new Object[rst.getMetaData().getColumnCount()];
            for (int i = 1; i <= rst.getMetaData().getColumnCount(); i++) {
                array[i - 1] = rst.getObject(i);
            }
            list.add(array);
        }

        //Close connection (no result set accesses after this point
        connection.close();

        if (list.size() == 0) return List.of();
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            Object[] pks = list.get(i);
            int finalI = i;
            //Do get in parallel
            es.execute(() -> {
                spawnGetThread(this, map, pks, finalI);
            });
        }
        es.shutdown();
        try {
            boolean success = es.awaitTermination(150, TimeUnit.SECONDS);
            if (!success) {
                throw new RuntimeException("Timeout in DAO getAll()");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ret = mapMapToList(entityType, map, list.size());
        return ret;
    }

    private <X> List<X> mapMapToList(Class<X> toClass, Map<Integer, Object> map, int size) {
        return IntStream.range(0, size).mapToObj(key -> toClass.cast(map.get(key))).collect(Collectors.toList());
    }

    private void spawnGetThread(Dao dao, ConcurrentHashMap<Integer, Object> map, Object[] pks, int position) {
        try {
            var opt = dao.getWithConnection(dataSource, pks);
            if (opt.isPresent()) {
                map.put(position, (Object) opt.get());
            }
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public Optional<E> get(Object... primaryKeyValues) throws SQLException {
        return getWithConnection(dataSource, primaryKeyValues);
    }

    private Optional<E> getWithConnection(DataSource dataSource, Object[] primaryKeyValues) throws SQLException { //this has a different name, because Object varargs are a bit too broad otherwise
        String sql = queryBuilder.createGetSQL(entityType);
        assert primaryKeyValues.length == Mapper.getPrimaryKeyFields(fields).size() : "wrong number of primaryKeyValues given";
        var connection = dataSource.getConnection();
        ResultSet rst = queryExecutor.executeQuery(connection, sql, primaryKeyValues);
        if (!rst.next()) {
            connection.close();
            return Optional.empty();
        }
        var reconstructed = constructNewInstance();

        //fill instance
        fillSavableWithPrimitives(reconstructed, rst);
        //Close connection after resultSet has been processed
        connection.close();

        var nonPrimitives = fields.stream().filter(f -> !f.isPrimitive()).collect(Collectors.toList());
        for (SQLField field : nonPrimitives) {
            if (!field.isArray() && !field.isList()) {
                var rightpks = relationGet(field, reconstructed);
                if (rightpks.size() < 1) {
                    continue;//No relation
                }
                if (rightpks.size() > 1) { //This should not occur, print error and continue gracefully
                    new RuntimeException("A non array has more than one relation objects, you probably inserted something into the db that you shouldn't have").printStackTrace();
                }
                Dao dao = new Dao(field.getFieldType(), dataSource);//Raw use to avoid errors
                var content = dao.get(rightpks.get(0));
                if (content.isEmpty()) continue;//Dao did not give a result
                field.setFieldContent(reconstructed, content.get());
            }
            if (field.isArray()) {
                var rightpks = relationGet(field, reconstructed);
                if (rightpks.size() < 1) {
                    continue;//No relation
                }
                Dao dao = new Dao(field.getFieldType(), dataSource);//Raw use to avoid errors
                var array = Array.newInstance(field.getFieldType(), rightpks.size());

                //Begin concurrent magic
                ExecutorService es = Executors.newCachedThreadPool();
                var map = new ConcurrentHashMap<Integer, Object>();


                for (int i = 0; i < rightpks.size(); i++) {
                    int finalI = i;
                    es.execute(() -> {
                        spawnGetThread(dao, map, rightpks.get(finalI), finalI);
                    });
                }

                es.shutdown();//finish setting up threads

                try {
                    boolean success = es.awaitTermination(150, TimeUnit.SECONDS);
                    if (!success) {
                        throw new RuntimeException("Timeout in DAO get()");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                var mappedList = mapMapToList(field.getFieldType(), map, rightpks.size());
                for (int i = 0; i < mappedList.size(); i++) {
                    Array.set(array, i, mappedList.get(i));
                }

                field.setFieldContent(reconstructed, array);
            }
            if (field.isList()) {
                assert false : "not implemented yet";
            }
        }
        callAfterConstruction(reconstructed);
        return Optional.of(reconstructed);
    }

    public Optional<E> insert(E toInsert) throws SQLException {

        callBeforeDeconstruction(toInsert);
        //Start with checking if object is already present (mainly used for recursion)
        var pkValues = Mapper.getPrimaryKeyFieldValues(entityType, toInsert);
        var o = get(pkValues.toArray());

        var connection = dataSource.getConnection();//Reserve after get() has finished

        if (o.isPresent()) { //Object with same PKs already inserted
            connection.close();
            return o;
        }
        //begin insertion of primitive types
        var fields = Mapper.getAllSQLFields(entityType);
        var insertableValues = fields.stream()
                .filter(SQLField::isInsertable)
                .map(f -> f.getFieldContent(toInsert))
                .collect(Collectors.toList());
        String insertSQL = queryBuilder.createInsertSQL(entityType);
        ResultSet rst = queryExecutor.executeQuery(connection, insertSQL, insertableValues.toArray());
        if (!rst.next()) {
            connection.close();
            return Optional.empty();//Insert failed for unknown reason (did not return anything)
        }
        //reconstruct object part 1 (primitive values only)
        E reconstructed = constructNewInstance();
//        System.out.println(reconstructed);
        fillSavableWithPrimitives(reconstructed, rst);
        //Primitives inserted and filled in

        //Close connection
        connection.close();

        //Start with insertion of non Primitives
        var nonPrimitives = fields.stream()
                .filter(f -> !f.isPrimitive())
                .collect(Collectors.toList());
        for (SQLField sqlField : nonPrimitives) {
            Class<? extends Savable> type = sqlField.getReferencingClass();
            Dao dao = new Dao<>(type, dataSource); //Has to be raw because compiler strikes otherwise
            if (!sqlField.isArray() && !sqlField.isList()) {
                //Process field as single Savable object
                var insertedOpt = dao.insert(type.cast(sqlField.getFieldContent(toInsert))); //insert the other object recursively
                if (insertedOpt.isPresent()) {
                    //insert into other table successful, now insert into relation table
                    var inserted = (Savable) insertedOpt.get();
                    relationInsert(sqlField, reconstructed, inserted);//do insertion into relation
                    sqlField.setFieldContent(reconstructed, inserted);
                }
            }
            if (sqlField.isArray()) {
                //Process field as Array of Savable objects
                var array = sqlField.getFieldContent(toInsert);
                assert array.getClass().isArray();
                int length = Array.getLength(array);
                List<Savable> insertedList = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    //insert and then save
                    var arrayObject = Array.get(array, i);
                    var insertedOpt = dao.insert(type.cast(arrayObject));
                    if (insertedOpt.isPresent()) {
                        //insert into other table successful, now insert into relation table
                        var inserted = (Savable) insertedOpt.get();
                        insertedList.add(inserted);
                        relationInsert(sqlField, reconstructed, inserted);//do insertion into relation
                    }
                }
                var insertedArray = Array.newInstance(type, insertedList.size());
                for (int i = 0; i < insertedList.size(); i++) {
                    Array.set(insertedArray, i, insertedList.get(i));
                }
                sqlField.setFieldContent(reconstructed, insertedArray);
            }
            if (sqlField.isList()) {
                //Process field as List of Savable Objects
                assert false : "List types not implemented yet, please mark it as @Ignored";
            }
        }
        callAfterConstruction(reconstructed);
        return Optional.of(reconstructed);
    }

    public E update(E toUpdate) throws SQLException {
        var connection = dataSource.getConnection();

        callBeforeDeconstruction(toUpdate);
        String sql = queryBuilder.createUpdateSQL(entityType); //Fill in all updated values first, then PK values again
        var pkvalues = Mapper.getPrimaryKeyFieldValues(fields, toUpdate);
        var allValues = fields.stream()
                .filter(SQLField::isPrimitive)
                .map(f -> f.getFieldContent(toUpdate))
                .collect(Collectors.toList());
        allValues.addAll(pkvalues);
        System.out.println(allValues);
        System.out.println(sql);
        ResultSet rst = queryExecutor.executeQuery(connection, sql, allValues.toArray());
        int count = rst.getMetaData().getColumnCount();
        if (!rst.next()){
            connection.close();
            return null;//Update failed? PKs not found anymore
        }
        Object[] pks = new Object[count];
        for (int i = 1; i <= count; i++) {
            pks[i - 1] = rst.getObject(i);
        }

        //close connection
        connection.close();

        var entityOpt = get(pks);
        if (entityOpt.isEmpty()) return null;//Update failed? PKs not found anymore
        //var entity=entityOpt.get();
        //TODO: update / insert? other entities
        if (followOnUpdate) {
            var nonPrimitivefields = fields.stream().filter(f -> !f.isPrimitive()).collect(Collectors.toList());
            for (SQLField sqlField : nonPrimitivefields) {
                Dao dao = new Dao(sqlField.getReferencingClass(), dataSource);
                //TODO: get and if not present, then insert (?)
                Class<? extends Savable> type = sqlField.getReferencingClass();
                if (!sqlField.isArray() && !sqlField.isList()) {
                    var optional = dao.get(Mapper.getPrimaryKeyFieldValues(type, sqlField.getFieldContent(toUpdate)).toArray());
                    Savable differententity = null;
                    if (optional.isPresent()) {
                        differententity = dao.update((Savable) sqlField.getFieldContent(toUpdate));
                    } else {
                        differententity = (Savable) dao.insert((Savable) sqlField.getFieldContent(toUpdate)).get();
                    }
                    //TODO: update relation table
                    relationRemove(sqlField, toUpdate);
                    relationInsert(sqlField, toUpdate, differententity);

                    sqlField.setFieldContent(toUpdate, differententity);
                }
                if (sqlField.isArray()) {
                    var array = sqlField.getFieldContent(toUpdate);
                    assert array.getClass().isArray();
                    int length = Array.getLength(array);
                    List<Savable> insertedList = new ArrayList<>();
                    relationRemove(sqlField, toUpdate);
                    for (int i = 0; i < length; i++) {
                        //insert and then save
                        var arrayObject = Array.get(array, i);
                        var arrayOpt = dao.get(Mapper.getPrimaryKeyFieldValues(type, arrayObject).toArray());
                        Savable insertedOpt;
                        if (arrayOpt.isPresent()) {
                            insertedOpt = dao.update(type.cast(arrayObject));
                        } else {
                            insertedOpt = (Savable) dao.insert(type.cast(arrayObject)).get();
                        }

                        if (insertedOpt != null) {
                            //insert into other table successful, now insert into relation table
                            var updated = (Savable) insertedOpt;
                            insertedList.add(updated);
                            relationInsert(sqlField, toUpdate, updated);
                        }
                    }
                    var insertedArray = Array.newInstance(type, insertedList.size());
                    for (int i = 0; i < insertedList.size(); i++) {
                        Array.set(insertedArray, i, insertedList.get(i));
                    }
                    sqlField.setFieldContent(toUpdate, insertedArray);
                }
            }
        }
        return toUpdate;
    }

    public void remove(E toRemove) throws SQLException {
        remove(toRemove, false);
    }

    public void remove(E toRemove, boolean deletechildren) throws SQLException {
        var nonPrimitiveFields = fields.stream()
                .filter(f -> !f.isPrimitive())
                .collect(Collectors.toList());
        for (SQLField sqlField : nonPrimitiveFields) {
            var pks = relationGet(sqlField, toRemove);
            relationRemove(sqlField, toRemove);
            Dao otherDao = new Dao(sqlField.getReferencingClass(), dataSource);
            for (Object[] pk : pks) {
                var otherToRemove = (Savable) otherDao.get(pk).get(); //if get fails, then the pks must have vanished in the db
                if (deletechildren) {
                    otherDao.remove(otherToRemove, deletechildren);
                }
            }
        }
        String sql = queryBuilder.createRemoveSQL(entityType);
        var connection = dataSource.getConnection();
        queryExecutor.executeStatement(connection, sql, Mapper.getPrimaryKeyFieldValues(entityType, toRemove).toArray());
        connection.close();
    }

    private boolean relationRemove(SQLField sqlField, Savable entity) throws SQLException {
        var type = sqlField.getReferencingClass();
        String sql = queryBuilder.createRelationRemoveSQL(sqlField);
        var connection = dataSource.getConnection();
        boolean b = queryExecutor.executeStatement(connection, sql, Mapper.getPrimaryKeyFieldValues(sqlField.getFieldClass(), entity).toArray());
        connection.close();
        return b;
    }

    private boolean relationInsert(SQLField sqlField, Savable reconstructed, Savable inserted) throws SQLException {
        var type = sqlField.getReferencingClass();
        String sql = queryBuilder.createRelationInsertSQL(sqlField);
        var relationLValues = Mapper.getPrimaryKeyFieldValues(entityType, reconstructed);
        var relationRValues = Mapper.getPrimaryKeyFieldValues(type, inserted);
        var relationInsertValues = new ArrayList<>();
        relationInsertValues.addAll(relationLValues);
        relationInsertValues.addAll(relationRValues);

        var connection = dataSource.getConnection();
        boolean b = queryExecutor.executeStatement(connection, sql, relationInsertValues.toArray()); //insert into relation
        connection.close();

        return b;
    }

    /**
     * @param sqlField      the field for which the values should be requested
     * @param reconstructed the object containing the left primary keys
     * @return the right primary keys of the relation table in a list
     * @throws SQLException
     */
    private List<Object[]> relationGet(SQLField sqlField, Savable reconstructed) throws SQLException {
        String sql = queryBuilder.createRelationGetRightSQL(sqlField);
//        System.out.println(entityType.getName()+" : "+reconstructed);
        var pks = Mapper.getPrimaryKeyFieldValues(entityType, reconstructed);

        var connection = dataSource.getConnection();
        ResultSet rst = queryExecutor.executeQuery(connection, sql, pks.toArray());
        int columncount = rst.getMetaData().getColumnCount();
        List<Object[]> list = new ArrayList<>();
        while (rst.next()) {
            var array = new Object[columncount];
            for (int i = 1; i <= columncount; i++) {
                array[i - 1] = rst.getObject(i);
            }
            list.add(array);
        }
        connection.close();
        return list;
    }


    private void fillSavableWithPrimitives(E entity, ResultSet rst) throws SQLException {
        var collect = fields.stream()
                .filter(SQLField::isPrimitive)
                .collect(Collectors.toList());
        for (SQLField sqlField : collect) {
//            System.out.println(sqlField);
            Object variable = rst.getObject(sqlField.getSQLFieldName());
            if (!sqlField.isPrimitiveArray()) {

                if (variable instanceof Timestamp) {
                    Timestamp tstamt = (Timestamp) variable;
                    variable = tstamt.toLocalDateTime();
                }
                sqlField.setFieldContent(entity, variable);

            } else if (sqlField.isArray()) { //If it is an array, then postgres will return an pgarray instead of an array
                var pgarray = (PgArray) variable;
                //I hate arrays (int[] != Integer[])
                var array = pgarray.getArray();
                var type = sqlField.getFieldType();
                var properArray = Array.newInstance(type, Array.getLength(array));
                for (int i = 0; i < Array.getLength(array); i++) {
                    Array.set(properArray, i, Array.get(array, i));
                }
                sqlField.setFieldContent(entity, properArray);
            }
        }
    }

    private void callBeforeDeconstruction(E entity) {
        try {
            var method = entityType.getDeclaredMethod("beforeDeconstruction");
            method.trySetAccessible();
            method.invoke(entity);
        } catch (NoSuchMethodException ignored) {
            //Method not declared (aka not needed)
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void callAfterConstruction(E entity) {
        try {
            var method = entityType.getDeclaredMethod("afterConstruction");
            method.trySetAccessible();
            method.invoke(entity);
        } catch (NoSuchMethodException ignored) {
            //Method not declared (aka not needed)
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private E constructNewInstance() {
        try { //create an "empty" instance of the class
            var constructor = entityType.getDeclaredConstructor();
            if (!constructor.trySetAccessible()) throw new RuntimeException("Accessing the Constructor failed");
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("The class " + entityType.getName() + " does not have a no args Constructor");
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Failed to instantiate the Class: " + entityType.getName(), e);
        }
    }
}
