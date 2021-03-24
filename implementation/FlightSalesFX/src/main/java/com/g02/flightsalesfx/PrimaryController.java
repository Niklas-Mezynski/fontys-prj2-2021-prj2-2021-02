package com.g02.flightsalesfx;

import java.io.IOException;

import com.g02.flightsalesfx.App;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("createPlane");
    }
}
