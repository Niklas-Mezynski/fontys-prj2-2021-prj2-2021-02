module com.g02.flightsalesfx {
    requires java.sql;
    requires BTFDao;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    opens org.g02.flightsalesfx;
    opens org.g02.flightsalesfx.businessEntities;
    opens org.g02.flightsalesfx.businessLogic;
    opens org.g02.flightsalesfx.persistence;
    exports org.g02.flightsalesfx;
    exports org.g02.flightsalesfx.businessEntities;
}
