module BTFDao {
    requires java.sql;
    requires java.naming;
    requires org.postgresql.jdbc;
    exports com.g02.btfdao.dao;
    exports com.g02.btfdao.annotations;
    exports com.g02.btfdao.queries;
    exports com.g02.btfdao.utils;
    opens com.g02.btfdao.utils;
}