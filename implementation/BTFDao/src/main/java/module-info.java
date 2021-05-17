module BTFDao {
    requires java.sql;
    requires java.naming;
    requires org.postgresql.jdbc;
    exports org.g02.btfdao.dao;
    exports org.g02.btfdao.annotations;
}