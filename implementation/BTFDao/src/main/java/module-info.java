module BTFDao {
    requires java.sql;
    requires java.naming;
    requires org.postgresql.jdbc;
    exports com.g02.btfdao.dao;
    exports com.g02.btfdao.annotations;
}