module BTFDao {
    requires java.sql;
    requires org.postgresql.jdbc;
    requires java.naming;
    exports com.g02.btfdao.dao;
    exports com.g02.btfdao.annotations;
    exports com.g02.btfdao.queries;
    exports com.g02.btfdao.utils;
}