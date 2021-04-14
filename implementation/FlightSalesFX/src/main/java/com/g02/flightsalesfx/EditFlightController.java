package com.g02.flightsalesfx;

import com.g02.flightsalesfx.businessEntities.Flight;
import com.g02.flightsalesfx.businessLogic.BusinessLogicAPIImpl;
import com.g02.flightsalesfx.businessLogic.ReoccurringFlightImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.InputMismatchException;

import static com.g02.flightsalesfx.App.businessLogicAPI;
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

    @FXML
    private CheckBox reOccurrCheckBox;

    @FXML
    private TextField intervalTextField;


    static Flight selectedFlight;
    private int cntForReOccurr;     // 1 = reoccurring flight

    public void initialize() throws IOException {
        //numeric textfield
        intervalTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;
            intervalTextField.setText(newValue.replaceAll("[^\\d]", ""));
        });


        if (selectedFlight == null) {
            App.setRoot("home");
            return;
        }

        cntForReOccurr = 0;

        if(selectedFlight.getClass().equals(ReoccurringFlightImpl.class)) {
            System.out.println("reOccuring opened");
            cntForReOccurr = 1;
            reOccurrCheckBox.setSelected(true);
            intervalTextField.setDisable(false);
            ReoccurringFlightImpl f = (ReoccurringFlightImpl) selectedFlight;
            intervalTextField.setText(String.valueOf(f.getInterval()));
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
    void editFlightOptions() throws IOException {
        setRoot("editFlightOptions");
    }

    @FXML
    void enableIntervalInput() {
        System.out.println("check");
        if(isCheckBoxSelected()) {
            intervalTextField.setDisable(false);
        } else{
            intervalTextField.setText("");
            intervalTextField.setDisable(true);
            //cntForReOccurr = 0;
        }
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

        //check for change in reoccurr
        if(!isCheckBoxSelected()) { //not reoccuring
            //check if selectedFlight WAS a reoccurring one but is not anymore
            if(cntForReOccurr == 1) {
                var f = (ReoccurringFlightImpl) selectedFlight;
                var updatedFlight = f.getFlight();

                if(App.persistenceAPI.getFlightStorageService(businessLogicAPI.getFlightManager()).remove(f)){
                    System.out.println("removed old");
                    if(businessLogicAPI.createFlightFromUI(updatedFlight)) {
                        exit();
                    }
                }
            }
            //normal flight with changes only in timing
            exit();
        } else { // reoccurring is selected
            if(!intervalTextField.getText().trim().isEmpty()) {
                var reOccFlight = App.businessLogicAPI.createReoccurringFlightFromUI(selectedFlight, Integer.parseInt(intervalTextField.getText()));
                if(reOccFlight) {
                    exit();
                }
            }
        }
    }


    //HELPER
    private boolean isCheckBoxSelected() {
        return reOccurrCheckBox.isSelected();
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
