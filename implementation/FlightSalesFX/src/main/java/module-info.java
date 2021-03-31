module com.g02.flightsalesfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.g02.flightsalesfx to javafx.fxml;
    opens com.g02.flightsalesfx.businessEntities to javafx.fxml;
    opens com.g02.flightsalesfx.persistence to javafx.fxml;
    opens com.g02.flightsalesfx.businessLogic to javafx.fxml;
    exports com.g02.flightsalesfx;
    exports com.g02.flightsalesfx.businessEntities;
    exports com.g02.flightsalesfx.businessLogic;
    exports com.g02.flightsalesfx.persistence;
}
