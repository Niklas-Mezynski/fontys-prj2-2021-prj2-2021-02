module org.g02.flightsalesfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires BTFDao;
    requires java.sql;
    opens org.g02.flightsalesfx;
    opens org.g02.flightsalesfx.businessEntities;
    opens org.g02.flightsalesfx.businessLogic;
    opens org.g02.flightsalesfx.persistence;
    exports org.g02.flightsalesfx;
    exports org.g02.flightsalesfx.businessEntities;
}
