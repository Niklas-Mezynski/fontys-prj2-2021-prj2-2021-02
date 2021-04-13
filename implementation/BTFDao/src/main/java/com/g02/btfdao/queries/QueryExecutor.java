package com.g02.btfdao.queries;

import com.g02.btfdao.annotations.ForeignKey;
import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import org.postgresql.util.PGobject;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public <E extends Savable> Optional<E> doGet(Connection connection, String sql, Class<E> aClass, Object... keyValues) {
        try (var pst = fillPreparedStatement(connection.prepareStatement(sql), keyValues)) {
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    var construct=Mapper.construct(aClass, resultSet);
                    var fields=Mapper.getFields(construct.getClass(), ForeignKey.class);
                    for (Field field : fields) {
                        String sql2="select %4$s from %1$s where %2$s=%3$s";
                        String sql2f=format(sql2,
                                Mapper.relationTableName(field),
                                Mapper.relationColumnLeftName(field),
                                keyValues[0],
                                Mapper.relationColumnRightName(field)
                                );
                        System.out.println(sql2f);
                        var pst2=fillPreparedStatement(connection.prepareStatement(sql2f));
                        ResultSet resultSet2 = pst2.executeQuery();
                        if(field.getType().isArray()) {

                            ArrayList<Object> arr = new ArrayList<>();
                            while (resultSet2.next()) {
                                arr.add(resultSet2.getObject(Mapper.relationColumnRightName(field)));
                            }
                            Class<?> componentType = field.getType().getComponentType();
                            var array=Array.newInstance(componentType,arr.size());
                            for (int i = 0; i < arr.size(); i++) {
                                Array.set(array,i,arr.get(i));
                            }
                            field.set(construct, array);
                        } else {
                            if (resultSet2.next()){
                                field.set(construct,resultSet2.getObject(Mapper.relationColumnRightName(field)));
                            }
                        }

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
        var a=Mapper.deconstructInsertableFields(e);
        try {
            var b=Mapper.getPrimaryKeyValues(e);
            var la=a.stream().map(Pair::value).collect(Collectors.toList());
            var lb=Arrays.stream(b).collect(Collectors.toList());
            la.addAll(lb);
            var pst=fillPreparedStatement(connection.prepareStatement(sql),la.toArray());
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(Mapper.construct(aClass, resultSet));
                }
            } catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            } catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            } catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
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
            if (aClass==null){
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
