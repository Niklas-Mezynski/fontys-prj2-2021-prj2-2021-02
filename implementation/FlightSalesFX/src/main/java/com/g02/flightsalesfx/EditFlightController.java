package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

import static com.g02.flightsalesfx.App.setRoot;

public class EditFlightController {

    @FXML
    private Label FlightNumberLabel;

    @FXML
    private TextField DepartureTime;

    @FXML
    private TextField ArrivaleTime;

    @FXML
    private DatePicker DepartureDate;

    @FXML
    private DatePicker ArrivaleDate;



    static Flight selectedFlight;

    public void initialize() throws IOException {
        if (selectedFlight == null) {
            App.setRoot("home");
            return;
        }
        FlightNumberLabel.setText("Flight number: " + selectedFlight.getFlightNumber());
        DepartureDate.setValue(selectedFlight.getDeparture().toLocalDate());
        DepartureTime.setText(getTimeAsString(selectedFlight.getDeparture()));
        ArrivaleDate.setValue(selectedFlight.getArrival().toLocalDate());
        ArrivaleTime.setText(getTimeAsString(selectedFlight.getArrival()));
    }

    @FXML
    void exit() throws IOException {
        selectedFlight = null;
        setRoot("home");
    }

    @FXML
    void save() throws IOException {
        LocalDateTime newDeparture = createLocalDateTimeFromTextField(DepartureDate.getValue(), DepartureTime.getText());
        LocalDateTime newArrival = createLocalDateTimeFromTextField(ArrivaleDate.getValue(), ArrivaleTime.getText());
        if (!newDeparture.isBefore(newArrival)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error while saving");
            alert.setContentText("Departure time must be before Arrival Time!");
            alert.showAndWait();
            return;
        }
        selectedFlight.setDeparture(newDeparture);
        selectedFlight.setArrival(newArrival);
        exit();
    }



    //HELPER
    private String getTimeAsString (LocalDateTime date) {
        String hours = String.valueOf(date.getHour());
        String minutes = String.valueOf(date.getMinute());
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }

    //HELPER
    private LocalDateTime createLocalDateTimeFromTextField (LocalDate startDate, String startTime) {
        String[] splittedField = startTime.split(":");

        if(splittedField.length > 0){
            if(splittedField.length == 2) {
                int hour = Integer.valueOf(splittedField[0].trim());
                int min = Integer.valueOf(splittedField[1].trim());

                return startDate.atTime(hour, min);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error during registration");
                alert.setContentText("The entered information regarding the departure are either not filled in or filled in wrongly." +
                        "Make sure the input looks like 'hh:mm'.");
                alert.showAndWait();
            }

        }

        throw new InputMismatchException("Wrong input-syntax. input must look like: 'hh:mm'.");
    }


}
