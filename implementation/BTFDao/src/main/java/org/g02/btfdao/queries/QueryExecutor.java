package org.g02.btfdao.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecutor {
    public ResultSet executeQuery(Connection connection, String sql, Object[] values) {
        try {
            var pst = connection.prepareStatement(sql);
            fillPreparedStatement(pst, values);
            var resultSet = pst.executeQuery();
            return resultSet;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }
    }

    public boolean executeStatement(Connection connection, String sql, Object[] values) {
        try {
            var pst = connection.prepareStatement(sql);
            fillPreparedStatement(pst, values);
            var ret=pst.execute();
            pst.close(); //Only a failsafe, please remember to close the connection in the dao as well
            return ret;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException(throwables);
        }
    }

    private void fillPreparedStatement(PreparedStatement pst, Object[] values) {
        for (int i = 1; i <= values.length; i++) {
            try {
                pst.setObject(i, values[i - 1]);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
