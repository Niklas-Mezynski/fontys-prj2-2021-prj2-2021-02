package com.g02.btfdao.queries;

import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import org.postgresql.util.PGobject;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

    private PreparedStatement fillPreparedStatement(final PreparedStatement preparedStatement, Object... keyValues) throws SQLException {
        for (int i = 0; i < keyValues.length; i++) {
            var obj = keyValues[i];
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
        return fillPreparedStatement(pst, pairs.stream().map(Pair::value));
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
}
