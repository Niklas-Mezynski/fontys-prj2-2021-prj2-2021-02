package com.g02.btfdao.queries;

import com.g02.btfdao.mapper.Mapper;
import com.g02.btfdao.utils.Pair;
import com.g02.btfdao.utils.Savable;
import org.postgresql.util.PGobject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class QueryExecutor{
    public <E extends Savable> Optional<E> doInsert(Connection connection, String Sql, E e){
        try (var pst=fillPreparedStatement(connection.prepareStatement(Sql), Mapper.deconstructInsertableFields(e))){
            try (ResultSet resultSet=pst.executeQuery()){
                if (resultSet.next()){
                    return Optional.of((E)Mapper.construct(e.getClass(),resultSet));
                }
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException instantiationException) {
                instantiationException.printStackTrace();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }
    private PreparedStatement fillPreparedStatement(final PreparedStatement pst, List<Pair<String, Object>> pairs) throws SQLException {
        for (int i = 0; i < pairs.size(); i++) {
            var obj=pairs.get(i).value();
            if(obj instanceof PGobject){
                pst.setObject(i+1,obj,1111);
                System.out.println("How did I get here?");
            }
            else{
                pst.setObject(i+1,obj);
            }
        }
        return pst;
    }
}
