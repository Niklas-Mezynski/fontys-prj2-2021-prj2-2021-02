package com.g02.btfdao.queries;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import org.postgresql.util.PGobject;
import org.postgresql.util.PSQLException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class QueryExecutor {
    public <E extends Savable> Optional<E> doInsert(Connection connection, String Sql, Class<E> aClass, E e) {
        try (var pst = fillPreparedStatement(connection.prepareStatement(Sql), Mapper.deconstructInsertableFields(e))) {
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Mapper.construct(aClass, resultSet));
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException instantiationException) {
                instantiationException.printStackTrace();
            }
        } catch (SQLException | IllegalAccessException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public <E extends Savable> Optional<E> doGet(Connection connection, String sql, Class<E> aClass, Object... keyValues) {
        try (var pst = fillPreparedStatement(connection.prepareStatement(sql), keyValues)) {
            System.out.println("PST: " + pst);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    var construct = Mapper.construct(aClass, resultSet);
                    var fields = Mapper.getFields(construct.getClass(), ForeignKey.class);
                    for (Field field : fields) {
                        if (Mapper.isDatabaseType(field) || !field.getType().isArray()) continue;
                        String sql2 = "select %4$s from %1$s where %2$s=%3$s";
                        String sql2f = format(sql2,
                                Mapper.relationTableName(field),
                                Mapper.relationColumnLeftName(field),
                                keyValues[0],
                                Mapper.relationColumnRightName(field)
                        );
                        System.out.println(sql2f);
                        var pst2 = fillPreparedStatement(connection.prepareStatement(sql2f));
                        ResultSet resultSet2 = pst2.executeQuery();
                        if (field.getType().isArray()) {

                            ArrayList<Object> arr = new ArrayList<>();
                            while (resultSet2.next()) {
                                arr.add(resultSet2.getObject(Mapper.relationColumnRightName(field)));
                            }
                            Class<?> componentType = field.getType().getComponentType();
                            var array = Array.newInstance(componentType, arr.size());
                            for (int i = 0; i < arr.size(); i++) {
                                Array.set(array, i, arr.get(i));
                            }
                            field.set(construct, array);
                        } else {
                            if (resultSet2.next()) {
                                field.set(construct, resultSet2.getObject(Mapper.relationColumnRightName(field)));
                            }
                        }

                    }
                    for (Field field : fields) {
                        if (!Mapper.isDatabaseType(field) || !field.getType().isArray()) continue;
                        //Object relation Table
                        String relationSQLtemplate = "SELECT %1$s FROM %2$s WHERE %3$s";
                        String relationSQL = String.format(relationSQLtemplate,
                                Arrays.stream(Mapper.getFields(Mapper.getReferencingClass(field), PrimaryKey.class)).map(s -> Mapper.getTableName(s.getDeclaringClass()) + "_" + Mapper.getSQLFieldName(s)).collect(Collectors.joining(", ")),
                                Mapper.relationTableName(field),
                                Arrays.stream(Mapper.getFields(aClass, PrimaryKey.class)).map(s -> format("%1$s = ?", Mapper.getTableName(s.getDeclaringClass()) + "_" + Mapper.getSQLFieldName(s))).collect(Collectors.joining(" and "))
                        );
                        System.out.println(relationSQL);
                        var pst2 = fillPreparedStatement(connection.prepareStatement(relationSQL), keyValues);
                        ResultSet rst = pst2.executeQuery();
                        List<Savable> retList = new ArrayList<>();
                        while (rst.next()) {
                            List<Object> keyValuesList = new ArrayList<>();
                            for (int i = 1; true; i++) {
                                Object key;
                                try {
                                    key = rst.getObject(i);
                                } catch (PSQLException e) {
                                    break;
                                }
                                keyValuesList.add(key);
                            }
                            var tmp = doGet(connection, new QueryBuilder().createGetSQL(Mapper.getReferencingClass(field)), Mapper.getReferencingClass(field), keyValuesList.toArray()).orElseGet(null);
                            retList.add(tmp);
                        }
                        var type = field.getType();
                        if (type.isArray()) {
                            var arraytype = type.getComponentType();
                            var oldArray = retList.toArray();
                            var newArray = Array.newInstance(arraytype, oldArray.length);
                            for (int i = 0; i < oldArray.length; i++) {
                                Array.set(newArray, i, oldArray[i]);
                            }
                            field.set(construct, newArray);
                        } else {
                            field.set(construct, retList.get(0)); //Untested
                        }
                    }
                    for (Field field : fields) {
                        if (!Mapper.isDatabaseType(field) || field.getType().isArray()) continue;
                        var keyV = Mapper.getPrimaryKeyValues((Savable) field.get(construct));
                        var a = doGet(connection, new QueryBuilder().createGetSQL((Class<? extends Savable>) field.getType()), (Class<? extends Savable>) field.getType(), keyV);
                        System.out.println("String: " + a);
                        field.set(construct, a.get());
                    }
                    var ret = Optional.of(construct);
                    return ret;
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public PreparedStatement fillPreparedStatement(final PreparedStatement preparedStatement, Object... keyValues) throws SQLException {
        for (int i = 0; i < keyValues.length; i++) {
            var obj = keyValues[i];
            System.out.println(obj);
            if (obj instanceof PGobject) {
                preparedStatement.setObject(i + 1, obj, 1111);
                System.out.println("How did I get here?");
            } else {
                preparedStatement.setObject(i + 1, obj);
            }
            System.out.println(preparedStatement);
        }
        return preparedStatement;
    }

    private PreparedStatement fillPreparedStatement(final PreparedStatement pst, List<Pair<String, Object>> pairs) throws SQLException {
        return fillPreparedStatement(pst, pairs.stream().map(Pair::value).toArray());
    }

    public <E extends Savable> List<E> doGetAll(Connection connection, String getSQL, Class<E> entityClass) {
        try (var pst = fillPreparedStatement(connection.prepareStatement(getSQL))) {
            try (ResultSet resultSet = pst.executeQuery()) {
                List<E> list = new ArrayList<>();
                while (resultSet.next()) {
                    list.add(Mapper.construct(entityClass, resultSet));
//                    return Optional.of(Mapper.construct(entityClass, resultSet));
                }
                return list;
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Collections.emptyList();
    }

    public <E extends Savable> Optional<E> doUpdate(Connection connection, String sql, Class<E> aClass, E e) {
        List<Pair<String, Object>> a = null;
        try {
            a = Mapper.deconstructInsertableFields(e);
        } catch (ClassNotFoundException | IllegalAccessException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
        try {
            var b = Mapper.getPrimaryKeyValues(e);
            var la = a.stream().map(Pair::value).collect(Collectors.toList());
            var lb = Arrays.stream(b).collect(Collectors.toList());
            la.addAll(lb);
            var pst = fillPreparedStatement(connection.prepareStatement(sql), la.toArray());
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Mapper.construct(aClass, resultSet));
                }
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
        } catch (IllegalAccessException | SQLException illegalAccessException) {
            illegalAccessException.printStackTrace();
        }
//        try (var pst = fillPreparedStatement(connection.prepareStatement(sql), Mapper.deconstructInsertableFields(e).stream().map(Pair::value).toArray(), Mapper.getPrimaryKeyValues(e))) {
//
//        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | SQLException instantiationException) {
//            instantiationException.printStackTrace();
//        }
        return Optional.empty();
    }

    public <E extends Savable> Optional<E> doRemove(Connection connection, String sql, Class<E> aClass, Object... keyValues) {
        a:
        try (var pst = fillPreparedStatement(connection.prepareStatement(sql), keyValues)) {
            if (aClass == null) {
                pst.execute();
                break a;
            }
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Mapper.construct(aClass, resultSet));
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

}