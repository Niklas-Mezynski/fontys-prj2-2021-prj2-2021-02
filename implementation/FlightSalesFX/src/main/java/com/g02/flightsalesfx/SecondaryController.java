package com.g02.flightsalesfx;

import java.io.IOException;

import com.g02.flightsalesfx.App;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}