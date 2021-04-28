package org.g02.flightsalesfx;

import org.g02.flightsalesfx.businessEntities.Flight;
import org.g02.flightsalesfx.helpers.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class priceReductionController implements Controller {

    @FXML
    private Label FlightNumberLabel;

    private Flight selectedFlight;

    public void initialize() {
        this.selectedFlight = EditFlightController.selectedFlight;
        FlightNumberLabel.setText("Edit price reductions for Flight: " + selectedFlight.getFlightNumber());
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
