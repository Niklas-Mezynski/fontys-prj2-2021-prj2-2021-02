package com.g02.flightsalesfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SubmitFlightController {

    @FXML
    private TextField planeSearchBar;

    @FXML
    private AnchorPane planeTablePane;

    @FXML
    private TextField flightPrice;

    @FXML
    private Button saveFlightButton;

    @FXML
    private Button exitFlightButton;

    @FXML
    void exit(ActionEvent event) {

    }

    @FXML
    void saveFlight(ActionEvent event) {
        var price = Double.valueOf(flightPrice.getText());
    }

}
