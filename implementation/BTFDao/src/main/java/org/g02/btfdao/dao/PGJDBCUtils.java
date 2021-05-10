package org.g02.btfdao.dao;

import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp2.*;

public class PGJDBCUtils {

    public static final String APPLICATION_PROPERTIES = "application.properties";
    private static final Map<String, DataSource> datasourceByName = new HashMap<>();

    public static DataSource getDataSource(final String sourceName) {
        File propFile = new File(APPLICATION_PROPERTIES);
        if (!propFile.exists()) {
            return null;
        }

        return datasourceByName.computeIfAbsent(sourceName,
                (s) -> {
                    Properties props = properties(APPLICATION_PROPERTIES);

                    var source = new BasicDataSource(); //This is now an Apache Data Source

                    String prefix = sourceName + ".";
                    String[] serverNames = {
                            props.getProperty(prefix + "dbhost")
                    };
                    source.setDriverClassName("org.postgresql.Driver");

                    String user = props.getProperty(prefix + "username");
                    source.setUsername(user);
                    source.setPassword(props
                            .getProperty(prefix + "password"));

                    source.setUrl("jdbc:postgresql://" + serverNames[0] + "/");
                    source.setInitialSize(50);
                    source.setMinIdle(5);
                    source.setMaxIdle(50);
                    source.setMaxOpenPreparedStatements(100);
                    //Begin check
                    String pingQuery = "SELECT current_database(), now()::TIMESTAMP as now;";
                    try (Connection con = source.getConnection();
                         PreparedStatement pst = con.prepareStatement(pingQuery);) {
                        try (ResultSet rs = pst.executeQuery();) {
                            if (rs.next()) {
                                Object db = rs.getObject("current_database");
                                Object now = rs.getObject("now");
                                System.out.println("connected to db " + db
                                        .toString() + ", date/time is " + now
                                        .toString());
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(PGJDBCUtils.class.getName()).log(
                                Level.SEVERE, null, ex);
                    }
                    return source;
                }
        );
    }

    static Properties properties(String propFileName) {
        Properties properties = new Properties();
        try (
                FileInputStream fis = new FileInputStream(propFileName);) {
            properties.load(fis);
        } catch (IOException ignored) {
            Logger.getLogger(PGJDBCUtils.class.getName()).log(
                    Level.INFO,
                    "attempt to read from well file from known location '",
                    ignored);
        }
        return properties;
    }
}
