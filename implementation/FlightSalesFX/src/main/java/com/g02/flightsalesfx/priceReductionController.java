package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class priceReductionController {

    @FXML
    private Label FlightNumberLabel;

    private Flight selectedFlight;

    void initialize() {
        this.selectedFlight = EditFlightController.selectedFlight;
    }

    @FXML
    void exit() throws IOException {
        selectedFlight = null;
        App.setRoot("home");
    }

    @FXML
    void save() throws IOException {
        exit();
    }

}
