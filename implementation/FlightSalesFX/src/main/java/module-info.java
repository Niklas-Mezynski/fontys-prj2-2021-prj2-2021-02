module com.g02.flightsalesfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.g02.flightsalesfx to javafx.fxml;
    exports com.g02.flightsalesfx;
}
