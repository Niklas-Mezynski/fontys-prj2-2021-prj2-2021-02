module com.g02.flightsalesfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.g02.flightsalesfx;
    opens com.g02.flightsalesfx.businessEntities;
    opens com.g02.flightsalesfx.businessLogic;
    opens com.g02.flightsalesfx.persistence;
    exports com.g02.flightsalesfx;
    exports com.g02.flightsalesfx.businessEntities;
}
