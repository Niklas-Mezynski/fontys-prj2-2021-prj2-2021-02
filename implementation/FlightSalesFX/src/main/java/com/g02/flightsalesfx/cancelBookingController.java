package com.g02.flightsalesfx;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class cancelBookingController {

    @FXML
    private TextField flightNumberTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private AnchorPane bookingFlightPane;

    @FXML
    void abortButtonPressed(ActionEvent event) throws IOException {
        App.setRoot("salesEmployeeHome");
    }

    @FXML
    void cancelTicketPressed(ActionEvent event) {

    }

}