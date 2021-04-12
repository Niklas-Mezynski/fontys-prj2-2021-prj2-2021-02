package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessEntities.FlightOption;
import com.g02.flightsalesfx.businessLogic.FlightOptionImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.g02.flightsalesfx.App.setRoot;

public class FlightOptionController {

    @FXML
    private Label flightNumberLabel;

    @FXML
    private TextField planeInfoText;

    @FXML
    private VBox mainVBox;

    @FXML
    private ListView<Label> optionList;

    @FXML
    private TextField optionName;

    @FXML
    private TextField optionMaxAvailable;

    @FXML
    private TextField optionPrice;

    private Flight selectedFlight;
    private List<FlightOption> newFlightOptions = new ArrayList<>();

    public void initialize () throws IOException {
        selectedFlight = EditFlightController.selectedFlight;
        if (selectedFlight == null) {
            App.setRoot("home");
            return;
        }
        flightNumberLabel.setText("Edit Flight Options for flight number: " + selectedFlight.getFlightNumber());
        planeInfoText.setText("Seats: " + selectedFlight.getPlane().getSeatCount());

        var flightOptions = selectedFlight.getFlightOptions();
        flightOptions.add(new FlightOptionImpl("Currywurst", 2, 5.50));
        flightOptions.add(new FlightOptionImpl("Tomatensaft",3, 2.20));

        for (FlightOption fo: flightOptions) {
            Label label = new Label(fo.toString());
            optionList.getItems().add(label);
        }

    }

    @FXML
    void addFlightOption() {
        FlightOption fo = App.businessLogicAPI.getOptionManager().createFlightOption(
                optionName.getText(),
                Integer.parseInt(optionMaxAvailable.getText()),
                Double.parseDouble(optionPrice.getText())
        );
        newFlightOptions.add(fo);
        optionList.getItems().add(new Label(fo.toString()));

    }

    @FXML
    void exit() throws IOException {
        selectedFlight = null;
        setRoot("home");
    }

    @FXML
    void save() throws IOException {
        selectedFlight.addAllFlightOptions(newFlightOptions);
        exit();
    }
}
