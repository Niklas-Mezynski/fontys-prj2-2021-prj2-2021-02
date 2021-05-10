module BTFDao {
    requires java.sql;
    requires java.naming;
    requires org.postgresql.jdbc;
    requires commons.dbcp2;
    requires java.management;
    exports org.g02.btfdao.dao;
    exports org.g02.btfdao.annotations;
}